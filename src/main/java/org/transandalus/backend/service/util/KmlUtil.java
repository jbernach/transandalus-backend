package org.transandalus.backend.service.util;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.transandalus.backend.domain.Track;

import javax.transaction.Transactional;
import java.io.StringWriter;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class KmlUtil {
    /**
     * Return a String containing the combined KML from specified tracks.
     * The returned kml doesn't duplicate the folders, instead it combines it.
     *
     * @param tracks       List of Kml tracks to merge
     * @param folderFilter Folder name. The merged KML will only collect elements inside that folder name if folderFilter is not null.
     * @return String with the merged KML.
     */
    @Transactional
    public String mergeTracksKml(Stream<Track> tracks, String folderFilter) {
        Kml mergedKml = new Kml();
        Document mergedDocument = mergedKml.createAndSetDocument();

        tracks.forEach(track -> {
            if (track != null && track.getContent() != null) {
                String kmlString = track.getContent();

                // Fix Google kml namespace (for old files)
                kmlString = kmlString.replace("xmlns=\"http://earth.google.com/kml/2.2\"", "xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\"");

                // From String to DOM
                Kml trackKml = Kml.unmarshal(kmlString);

                if (trackKml != null) {
                    Document trackDocument = (Document) trackKml.getFeature();
                    trackDocument.getFeature().stream().forEach(f -> {
                        // Check if we're adding a folder that exists in the composed document (dont want duplicated forlders)
                        if (f instanceof Folder) {
                            if (folderFilter == null || folderFilter.equalsIgnoreCase(f.getName())) {
                                Optional<Feature> existingFolder = getFolder(mergedDocument, f.getName());

                                if (existingFolder.isPresent()) {
                                    // Only add the contents of the folder to existing one
                                    ((Folder) f).getFeature().stream().forEach(inFolderFeat -> {
                                        ((Folder) existingFolder.get()).addToFeature(inFolderFeat);
                                    });
                                } else {
                                    mergedDocument.addToFeature(f); // Add the folder and contents to merged kml
                                }
                            }
                        } else {
                            mergedDocument.addToFeature(f); // Add the feature to merged kml
                        }
                    });

                    // Copy styles without repeating
                    trackDocument.getStyleSelector().stream().forEach(style -> {
                        if (!hasStyleWithId(mergedDocument, style.getId())) {
                            mergedDocument.addToStyleSelector(style);
                        }
                    });
                }
            }
        });

        // String serialization
        StringWriter writer = new StringWriter();
        mergedKml.marshal(writer);

        return writer.toString();
    }

    /**
     * Return the Folder feature inside the Kml document.
     *
     * @param document Kml document
     * @param name     Folder name to search
     * @return Folder feature
     */
    public Optional<Feature> getFolder(Document document, String name) {
        return document.getFeature().stream().filter(f -> {

            if (f instanceof Folder && ObjectUtils.nullSafeToString(f.getName()).equalsIgnoreCase(ObjectUtils.nullSafeToString(name))) {
                return true;
            }
            return false;
        }).findAny();
    }

    /**
     * Return wheter the document contains a style with the specified Id.
     *
     * @param document Kml document
     * @param styleId  Style id to look for in the document
     * @return Wheter the document has the style or not
     */
    public boolean hasStyleWithId(Document document, String styleId) {
        return document.getStyleSelector().stream().filter(st -> ObjectUtils.nullSafeToString(st.getId()).equals(styleId)).count() > 0;
    }
}
