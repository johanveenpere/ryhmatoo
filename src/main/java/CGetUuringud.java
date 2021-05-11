import com.pixelmed.dicom.*;
import com.pixelmed.network.*;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CGetUuringud implements Serveriühendus {

    public static void main(String[] args) {
        CGetUuringud ühendus = new CGetUuringud();
        ühendus.TõmbaUuringud("HTYKRG12103221BB", "kaust");
    }

    @Override
    public String TõmbaUuringud(String pildiviit, String failiTee) {
        try {
            SpecificCharacterSet specificCharacterSet = new SpecificCharacterSet((String[]) null);
            AttributeList identifier = new AttributeList();

            identifier.putNewAttribute(TagFromName.QueryRetrieveLevel).addValue("STUDY");
            identifier.putNewAttribute(TagFromName.AccessionNumber).addValue(pildiviit);
            identifier.putNewAttribute(TagFromName.SOPInstanceUID);
            identifier.putNewAttribute(TagFromName.StudyInstanceUID);
            identifier.putNewAttribute(TagFromName.SOPClassesInStudy);

            new FindSOPClassSCU("www.dicomserver.co.uk", 104, "MEDCONNEC", "OURCLIENT", SOPClass.StudyRootQueryRetrieveInformationModelFind, identifier, new fileGetter("dicomserver.co.uk", "dicomserver", 11112, "./uuringud/"));

        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }

        return failiTee;
    }
}
    class fileGetter extends IdentifierHandler{
        private String SCPAddress;
        private String SCPTitle;
        private int port;
        private File filePath;

        public fileGetter(String SCPAddress, String SCPTitle, int port, String filePath) {
            this.SCPAddress = SCPAddress;
            this.SCPTitle = SCPTitle;
            this.port = port;
            this.filePath = new File(filePath);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void doSomethingWithIdentifier(AttributeList attributeListForFindResult) throws DicomException {
            System.out.println("Matched result:" + attributeListForFindResult);

            String studyInstanceUID = attributeListForFindResult.get(TagFromName.StudyInstanceUID)
                    .getSingleStringValueOrEmptyString();
            System.out.println("studyInstanceUID of matched result:" + studyInstanceUID);

            Set<String> setofSopClassesExpected = new HashSet<String>();
            Attribute sopClassesInStudy = attributeListForFindResult.get(TagFromName.SOPClassesInStudy);
            if (sopClassesInStudy != null) {
                String[] sopClassesInStudyList = sopClassesInStudy.getStringValues();
                for (String sopClassInStudy : sopClassesInStudyList) {
                    setofSopClassesExpected.add(sopClassInStudy);
                }
            } else {
                //if SOP class data for study is not found, then supply all storage SOP classes
                setofSopClassesExpected = (Set<String>) SOPClass.getSetOfStorageSOPClasses();
            }

            try {

                AttributeList identifier = new AttributeList();
                {
                    AttributeTag tag = TagFromName.QueryRetrieveLevel;
                    Attribute attribute = new CodeStringAttribute(tag);
                    attribute.addValue("STUDY");
                    identifier.put(tag, attribute);
                }
                {
                    AttributeTag tag = TagFromName.StudyInstanceUID;
                    Attribute attribute = new UniqueIdentifierAttribute(tag);
                    attribute.addValue(studyInstanceUID);
                    identifier.put(tag, attribute);
                }

                //please see PixelMed documentation if you want to dig deeper into the parameters and their relevance
                new GetSOPClassSCU(SCPAddress, port, SCPTitle, "javaclient", SOPClass.StudyRootQueryRetrieveInformationModelGet, identifier, new IdentifierHandler(), filePath, StoredFilePathStrategy.BYSOPINSTANCEUIDINSINGLEFOLDER, new OurCGetOperationStoreHandler(), setofSopClassesExpected, 0, true, false, false);
            } catch (Exception e) {
                System.out.println("Error during get operation" + e); // in real life, do something about this exception
                e.printStackTrace(System.err);
            }
        }
    }

    class OurCGetOperationStoreHandler extends ReceivedObjectHandler {

        @Override
        public void sendReceivedObjectIndication(String filename, String transferSyntax, String calledAetTitle)
                throws DicomNetworkException, DicomException, IOException {

            System.out.println("Incoming data from " + calledAetTitle + "...");
            System.out.println("filename:" + filename);
            System.out.println("transferSyntax:" + transferSyntax);

        }

    }


