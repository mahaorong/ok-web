package com.geek.okweb.baidu.ueditor.upload;

import com.geek.okweb.baidu.ueditor.PathFormat;
import com.geek.okweb.baidu.ueditor.define.AppInfo;
import com.geek.okweb.baidu.ueditor.define.BaseState;
import com.geek.okweb.baidu.ueditor.define.FileType;
import com.geek.okweb.baidu.ueditor.define.State;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class BinaryUploader {

	public static final State save(HttpServletRequest request,
								   Map<String, Object> conf) {
		// FileItemStream fileStream = null;
		// boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		// ServletFileUpload upload = new ServletFileUpload(
		// 	new DiskFileItemFactory());
		//
		// if ( isAjaxUpload ) {
		//     upload.setHeaderEncoding( "UTF-8" );
		// }

		try {
			// FileItemIterator iterator = upload.getItemIterator(request);
			//
			// while (iterator.hasNext()) {
			// 	fileStream = iterator.next();
			//
			// 	if (!fileStream.isFormField())
			// 		break;
			// 	fileStream = null;
			// }
			//
			// if (fileStream == null) {
			// 	return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			// }
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile(conf.get("fieldName").toString());
			if(multipartFile==null){
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			String savePath = (String) conf.get("savePath");
			//String originFileName = fileStream.getName();
			String originFileName = multipartFile.getOriginalFilename();
			String suffix = FileType.getSuffixByFilename(originFileName);

			originFileName = originFileName.substring(0,
					originFileName.length() - suffix.length());
			savePath = savePath + suffix;

			long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);

//			String physicalPath = (String) conf.get("rootPath") + savePath;
			/*String basePath = (String) conf.get("basePath");
			String physicalPath = basePath + savePath;*/

			//InputStream is = fileStream.openStream();
			InputStream is = multipartFile.getInputStream();
			State storageState = StorageManager.saveFileByInputStream(is,
					savePath, maxSize);
			is.close();

			if (storageState.isSuccess()) {
//				storageState.putInfo("url", PathFormat.format(savePath));
				String str = savePath.substring(savePath.lastIndexOf("/"));
				String path = "";
				Integer actionCode = (Integer) conf.get("actionCode");
				switch (actionCode){
					case 1:
						path = "/imagePath" + str;
						break;
					case 3:
						path = "/video" + str;
						break;
					default:
						path = "/imagePath" + str;
						break;
				}
				log.info("【路径】={}",path);
				storageState.putInfo("url",path);
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
			}

			return storageState;
			// } catch (FileUploadException e) {
			// 	return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}

