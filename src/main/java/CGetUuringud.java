import com.pixelmed.dicom.*;
import com.pixelmed.network.*;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class C_OP_Serveriühendus implements Serveriühendus {
    @Override
    public Andmed TõmbaUuringud(String pildiviit) {
        //DICOM C-FIND
        try {
            // use the default character set for VR encoding - override this as necessary
            SpecificCharacterSet specificCharacterSet = new SpecificCharacterSet((String[]) null);
            AttributeList identifier = new AttributeList();

            //build the attributes that you would like to retrieve as well as passing in any search criteria
            identifier.putNewAttribute(TagFromName.QueryRetrieveLevel).addValue("STUDY");
            identifier.putNewAttribute(TagFromName.AccessionNumber).addValue(pildiviit);
            //PatientName
            identifier.putNewAttribute(TagFromName.PatientName);
            identifier.putNewAttribute(TagFromName.PatientID,specificCharacterSet);
            identifier.putNewAttribute(TagFromName.SOPInstanceUID);
            //Image and Fluoroscopy Area Dose Product
            identifier.putNewAttribute(new AttributeTag("(0x0010,0x1010)"));
            identifier.putNewAttribute(new AttributeTag("(0x0010,0x0040)"));
            identifier.putNewAttribute(TagFromName.StudyInstanceUID);
            identifier.putNewAttribute(TagFromName.SOPClassesInStudy);

            new FindSOPClassSCU("www.dicomserver.co.uk",
                    104,
                    "MEDCONNEC",
                    "OURCLIENT",
                    SOPClass.StudyRootQueryRetrieveInformationModelFind, identifier,
                    new fileGetter("dicomserver.co.uk", "dicomserver", 11112, "./uuringud/"));

        } catch (Exception e) {
            e.printStackTrace(System.err); // in real life, do something about this exception
            System.exit(0);
        }


        return null;
    }
}
    class fileGetter extends IdentifierHandler{
        private String SCPAddress;
        private String SCPTitle;
        private int port;
        private File filePath;

        public static int resultsFound = 0;

        public fileGetter(String SCPAddress, String SCPTitle, int port, String filePath) {
            this.SCPAddress = SCPAddress;
            this.SCPTitle = SCPTitle;
            this.port = port;
            this.filePath = new File(filePath);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void doSomethingWithIdentifier(AttributeList attributeListForFindResult) throws DicomException {
            resultsFound++;
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
                new GetSOPClassSCU(SCPAddress,
                        port,
                        SCPTitle,
                        "javaclient",
                        SOPClass.StudyRootQueryRetrieveInformationModelGet,
                        identifier,
                        new IdentifierHandler(), //override and provide your own handler if you need to do anything else
                        filePath,
                        StoredFilePathStrategy.BYSOPINSTANCEUIDINSINGLEFOLDER,
                        new OurCGetOperationStoreHandler(),
                        setofSopClassesExpected,
                        0,
                        true,
                        false,
                        false);

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


