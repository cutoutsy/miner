package action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import java.io.File;

/**
 * 文件上传
 */
public class FileUploadAction extends ActionSupport {

    private File upload;
    private String uploadContentType;
    private String uploadFileName;
    private String result;

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public File getUpload() {
        return upload;
    }

    public String getUploadContentType() {
        return uploadContentType;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String execute() throws Exception{

        String path = ServletActionContext.getServletContext().getRealPath("/files");
        File file = new File(path);
        if(!file.exists()){
            file.mkdir();
        }

        FileUtils.copyFile(upload, new File(file, uploadFileName));

        result = "upload success";

        return SUCCESS;
    }

}
