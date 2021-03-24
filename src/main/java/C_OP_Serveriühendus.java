import com.pixelmed.network.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class C_OP_Serveriühendus implements Serveriühendus {
    @Override
    public Andmed SaaMetaandmed(String pildiviit) {
        AssociationFactory assoc = new AssociationFactory();
        PresentationContext context = new PresentationContext((byte) 1, "transferUID", "abstractUID");
        LinkedList<PresentationContext> contextList = new LinkedList<>();
        contextList.add(context);
        SCUSCPRoleSelection roleSelection = new SCUSCPRoleSelection("UID", false, false);
        LinkedList<SCUSCPRoleSelection> roleSelectionList = new LinkedList<>();
        roleSelectionList.add(roleSelection);
        try {
            assoc.createNewAssociation("www.dicomserver.co.uk", 104, "calledAETitle", "callingAETitle", contextList, roleSelectionList, false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DicomNetworkException e) {
            e.printStackTrace();
        }
        //FindSOPClassSCU sopFinder = new FindSOPClassSCU();
        return null;
    }
}
