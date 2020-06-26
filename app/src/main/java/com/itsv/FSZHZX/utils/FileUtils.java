package com.itsv.FSZHZX.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.Objects;

public class FileUtils {
    private String apkDirPath = "";
    private Context context;
    private String rootPath = Environment.getExternalStorageDirectory().getPath();

    public FileUtils(Context context) {
		mDataRootPath = context.getCacheDir().getPath();
        this.context = context;
		makeAppDir();
    }

	public String makeAppDir(){
		String path = getStorageDirectory();
		File folderFile = new File(path);
		if(!folderFile.exists()){
			folderFile.mkdir();
		}
		path = path + CACHE;
		folderFile = new File(path);
		if(!folderFile.exists()){
			folderFile.mkdir();
		}
		return path;
	}

    public String getApkDirPath() {
        return Objects.requireNonNull(context.getExternalFilesDir("apk")).getPath();
    }

    public String createFileDir() {
        return Objects.requireNonNull(context.getExternalFilesDir("pdf")).getPath();
    }
	private static final String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
	private final static String FOLDER_NAME = "/com.itsv.FSZHZX";

	public final static String CACHE = "/cache";
	private static String mDataRootPath = null;
	public String getStorageDirectory(){
		String localPath = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
		File folderFile = new File(localPath);
		if(!folderFile.exists()){
			folderFile.mkdir();
		}
		return localPath;
	}
	public String  getCachePath() {
		String storageDirectory = getStorageDirectory();
		return storageDirectory + CACHE;
	}

    //	/**
//	 * sd卡的根目录
//	 */
//	private static final String mSdRootPath = getExternalFilesDir
//	/**
//	 * 手机的缓存根目录
//	 */
//	private static String mDataRootPath = null;
//	/**
//	 * 保存Image的目录名
//	 */
//    private final static String FOLDER_NAME = "/fangshanZX";
//
//	public final static String IMAGE_NAME = "/cache";
//
//	public FileUtils(Context context){
//		mDataRootPath = context.getCacheDir().getPath();
//		makeAppDir();
//	}
//
//	public String makeAppDir(){
//		String path = getStorageDirectory();
//		File folderFile = new File(path);
//		if(!folderFile.exists()){
//			folderFile.mkdir();
//		}
//		path = path + IMAGE_NAME;
//		folderFile = new File(path);
//		if(!folderFile.exists()){
//			folderFile.mkdir();
//		}
//		return path;
//	}
//
//	public String  getCachePath() {
//		String storageDirectory = getStorageDirectory();
//		return storageDirectory + IMAGE_NAME;
//	}
//
//	/**
//	 * 获取储存Image的目录
//	 * @return
//	 */
//	public String getStorageDirectory(){
//		String localPath = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
//				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
//		File folderFile = new File(localPath);
//		if(!folderFile.exists()){
//			folderFile.mkdir();
//		}
//		return localPath;
//	}
//
//	public String getMediaVideoPath(){
//		String directory = getStorageDirectory();
//		directory += "/video";
//		File file = new File(directory);
//		if(!file.exists()){
//			file.mkdir();
//		}
//		return directory;
//	}
//
//	/**
//	 * 删除文件
//	 */
//	public void deleteFile(String deletePath,String videoPath) {
//		File file = new File(deletePath);
//		if (file.exists()) {
//			File[] files = file.listFiles();
//			for (File f : files) {
//				if(f.isDirectory()){
//					if(f.listFiles().length==0){
//						f.delete();
//					}else{
//						deleteFile(f.getAbsolutePath(),videoPath);
//					}
//				}else if(!f.getAbsolutePath().equals(videoPath)){
//					f.delete();
//				}
//			}
//		}
//	}

    /**
     * 创建文件夹
     */
    public static String createDir(String dirPath) {
        //因为文件夹可能有多层，比如:  a/b/c/ff.txt  需要先创建a文件夹，然后b文件夹然后...
        try {
            File file = new File(dirPath);
            if (Objects.requireNonNull(file.getParentFile()).exists()) {
                file.mkdir();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.mkdir();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }

}
