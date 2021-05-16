//Algne kood võetud lehelt: https://saravanansubramanian.com/dicomqueryretrievecmove/

import com.pixelmed.dicom.*;
import com.pixelmed.network.*;

import java.io.File;
import java.io.IOException;

public class CMoveUuringud implements Serveriühendus{

    final int port;
    final String address;
    final String moveUserTitle;
    final String moveProviderTitle;
    final String storeProviderTitle;

    public CMoveUuringud(int port, String address, String moveUserTitle, String moveProviderTitle, String storeProviderTitle) {
        this.port = port;
        this.address = address;
        this.moveUserTitle = moveUserTitle;
        this.moveProviderTitle = moveProviderTitle;
        this.storeProviderTitle = storeProviderTitle;
    }

    public static void main(String[] args) {
        CMoveUuringud ühendus = new CMoveUuringud(11112, "dicomserver.co.uk", "rando", "rando2", "rando");
        ühendus.TõmbaUuringud("HTYKRG12103221BB", "./kaust/");
    }
    @Override
    public String TõmbaUuringud(String pildiviit, String failiTee) {
        try {

            int storeScpPortNumber = port;
            File pathToStoreIncomingDicomFiles = new File(failiTee);

            Thread thread = new Thread(new StorageSOPClassSCPDispatcher(storeScpPortNumber, storeProviderTitle, pathToStoreIncomingDicomFiles, StoredFilePathStrategy.BYSOPINSTANCEUIDINSINGLEFOLDER, new OurCMoveDemoStoreHandler()));
            thread.start();

            SpecificCharacterSet specificCharacterSet = new SpecificCharacterSet((String[])null);
            AttributeList identifier = new AttributeList();

            identifier.putNewAttribute(TagFromName.QueryRetrieveLevel).addValue("STUDY");
            identifier.putNewAttribute(TagFromName.AccessionNumber).addValue(pildiviit);
            identifier.putNewAttribute(TagFromName.SOPInstanceUID);
            identifier.putNewAttribute(TagFromName.StudyInstanceUID);
            identifier.putNewAttribute(TagFromName.SOPClassesInStudy);
            identifier.putNewAttribute(TagFromName.PatientName);

            findHandler handler = new findHandler(address, moveProviderTitle, port, moveUserTitle, storeProviderTitle);
            new FindSOPClassSCU(address,
                    port,
                    moveProviderTitle,
                    moveUserTitle,
                    SOPClass.StudyRootQueryRetrieveInformationModelFind,
                    identifier,
                    handler);

        }
        catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        return failiTee;
    }

}

class findHandler extends IdentifierHandler {

    private String moveSCPAddress;
    private String moveScpAeTitle;
    private int moveScpPortNumber;
    private String moveScuAeTitle;
    private String storeScpAeTitle;

    public findHandler (String moveSCPAddress, String moveScpAeTitle, int moveScpPortNumber, String moveScuAeTitle, String storeScpAeTitle) {
        this.moveSCPAddress = moveSCPAddress;
        this.moveScpAeTitle = moveScpAeTitle;
        this.moveScpPortNumber = moveScpPortNumber;
        this.moveScuAeTitle = moveScuAeTitle;
        this.storeScpAeTitle = storeScpAeTitle;
    }

    @Override
    public void doSomethingWithIdentifier(AttributeList attributeListForFindResult) throws DicomException {
        System.out.println("Matched result:" + attributeListForFindResult);

        String studyInstanceUID = attributeListForFindResult.get(TagFromName.StudyInstanceUID).getSingleStringValueOrEmptyString();

        try {
            AttributeList identifier = new AttributeList();
            { AttributeTag tag = TagFromName.QueryRetrieveLevel; Attribute attribute = new CodeStringAttribute(tag); attribute.addValue("STUDY"); identifier.put(tag,attribute); }
            { AttributeTag tag = TagFromName.StudyInstanceUID; Attribute attribute = new UniqueIdentifierAttribute(tag); attribute.addValue(studyInstanceUID); identifier.put(tag,attribute); }


            new MoveSOPClassSCU(moveSCPAddress, moveScpPortNumber,moveScpAeTitle,moveScuAeTitle,storeScpAeTitle,SOPClass.StudyRootQueryRetrieveInformationModelMove,identifier);
        }
        catch (Exception e) {
            System.out.println("Error during move operation" + e);
            e.printStackTrace(System.err);
        }
    }

}

class OurCMoveDemoStoreHandler extends ReceivedObjectHandler {

    @Override
    public void sendReceivedObjectIndication(String filename, String transferSyntax, String calledAetTitle) throws DicomNetworkException, DicomException, IOException {

        System.out.println("Incoming data from " + calledAetTitle + "...");
        System.out.println("filename:" + filename);
        System.out.println("transferSyntax:" + transferSyntax);

    }

}
