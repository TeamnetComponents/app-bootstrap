package ro.teamnet.bootstrap.service.plugin;

public class FileUploadServiceUtil {

    public static String getGroupFor(String groupId){
        if(groupId!=null){
            return groupId;
        }else{
            return "defaultGroup";
        }
    }
}
