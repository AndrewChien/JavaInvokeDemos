/**
 * 用途：jna 调用DLL测试
 */
package ipcast;

import java.util.ArrayList;
import java.util.List;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public interface IPCastSDK  extends StdCallLibrary {

	IPCastSDK INSTANCE = (IPCastSDK) Native.loadLibrary("IPCast_I", IPCastSDK.class);
	
	/**
	 * 单曲播放（即只播放一次）
	 */
	public static final int PLAY_CYC_DAN = 0x0001;    //  单曲播放（即只播放一次）
	public static final int PLAY_CYC_DANCIRCLE = 0x0002;  //  单曲循环播放（循环播放一个曲目）
	public static final int PLAY_CYC_DANORDE = 0x0003;  //  顺序播放（按序播放全部歌曲一次）
	public static final int PLAY_CYC_ALLCIRCLE = 0x0004;  //  循环播放（循环播放所有歌曲）
	
	public static final int PLAY_CTRL_STOP = 1;    //  停止广播
	public static final int PLAY_CTRL_JUMPFILE = 2;  //  跳转至第 pos 曲播放
	public static final int PLAY_CTRL_NEXT = 3;    //  跳至下一曲播放
	public static final int PLAY_CTRL_PREV = 4;    //  跳至上一曲播放
	public static final int PLAY_CTRL_PAUSE = 5;    //  暂停播放
	public static final int PLAY_CTRL_RESTORE = 6;  //  恢复播放
	public static final int PLAY_CTRL_JUMPTIME = 7;  //  跳转到当前曲 pos 秒处位置
	
	public static final int MAX_NAME=256;
	
	
	/**
	 * 播放文件结构体
	 */
	public static class PlayFile extends Structure
	{
		public static class ByReference extends IPCastSDK.PlayFile implements Structure.ByReference {}  
	    public static class ByValue extends IPCastSDK.PlayFile implements Structure.ByValue {} 
	    
		public int      fid;            //  文件序号（序号小于 0 则取全路径）
		public byte[]   fname=new byte[MAX_NAME];    //  文件名或全路径
		public int		fvol;					// 文件音量放大倍数(除以10倍)
		
		
	}
	/**
	 * 服务器文件属性结构体
	 */
	public static class FileAttr extends Structure 
	{
		public int fid;
		public int attr;
		public int length;
		public byte[]   fname=new byte[MAX_NAME];
		public static class ByReference extends FileAttr  implements Structure.ByReference { }  
        public static class ByValue extends FileAttr implements Structure.ByValue{ }
        @Override
        protected List getFieldOrder(){
        	List fidList=new ArrayList();
        	fidList.add("fid");
        	fidList.add("attr");
        	fidList.add("length");
        	fidList.add("fname");
        	return fidList;
        	
        }
	}
	//文件上传结构体
		public static class UploadFileAttr extends Structure
		{
		 public	byte[] localName=new byte[MAX_NAME];
		 public	byte[] mediaName=new byte[MAX_NAME];	
			//public String localName;
			//public String mediaName;
			
		 public	int parantId;
		 public boolean bPrivate;
			public static class ByReference extends UploadFileAttr  implements Structure.ByReference { }  
	        public static class ByValue extends UploadFileAttr implements Structure.ByValue{ }
	        @Override
	        protected List getFieldOrder(){
	        	List upFileList=new ArrayList();
	        	upFileList.add("localName");
	        	upFileList.add("mediaName");
	        	upFileList.add("parantId");
	        	upFileList.add("bPrivate");
	        	return upFileList;
	        }
		}
	/**
	 * 对讲/寻呼结构体
	 */
	public static class CallAddr extends Structure
	{
		public int tid;
		public int box_id;
		public static class ByReference extends CallAddr  implements Structure.ByReference { }  
        public static class ByValue extends CallAddr implements Structure.ByValue{ }
        @Override
        protected List getFieldOrder(){
        	List talkList=new ArrayList();
        	talkList.add("tid");
        	talkList.add("box_id");
         	return talkList;
         }
	}

	/**
	 * 初始化API
	 */
	public boolean IPCAST_SDKInit();
	/**
	 * 释放API接口
	 */
	public boolean IPCAST_SDKRealease();
	/**
	 * 登录服务器
	 */
	public boolean IPCAST_Connect(String ipAddr, String user, String pwd);
	
	/**
	 * 退出登录
	 */
	public void IPCAST_DisConnect();
	
	
	/**
	 * 播放文件
	 * @param pfslist     广播文件列表
	 * @param fCount     广播文件的数目
	 * @param pTList     需要添加到本文件广播会话的广播终端列表
	 * @param tCount     需要添加的终端数目
	 * @param Grade      广播等级（0~999，数值越大级别越高）（整数）
	 * @param CycMode    播放模式
							PLAY_CYC_DAN = 0x0001;    //  单曲播放（即只播放一次）
							PLAY_CYC_DANCIRCLE = 0x0002;  //  单曲循环播放（循环播放一个曲目）
							PLAY_CYC_DANORDE = 0x0003;  //  顺序播放（按序播放全部歌曲一次）
							PLAY_CYC_ALLCIRCLE = 0x0004;  //  循环播放（循环播放所有歌曲）
	 * @param CycCount   循环播放次数。（即要求循环播放多少次，0：表示无限次）
	 * @param CycTime    播放时长（只有当 CycCount = 0 时有效，单位为秒。）
	 * @return
	 *    返回:  大于 0:  返回广播会话 ID；-1：会话创建失败 
	 */
	public int IPCAST_FilePlayStart(PlayFile.ByReference[] pFList, int fCount, int [] pTList, int tCount,
			int Grade, int CycMode, int CycCount, int CycTime);


	/**
	 *  播放文件控制
	 * @param sid   广播会话 ID
	 * @param cmd   控制命令
					PLAY_CTRL_STOP = 1;    //  停止广播
					PLAY_CTRL_JUMPFILE = 2;  //  跳转至第 pos 曲播放
					PLAY_CTRL_NEXT = 3;    //  跳至下一曲播放
					PLAY_CTRL_PREV = 4;    //  跳至上一曲播放
					PLAY_CTRL_PAUSE = 5;    //  暂停播放
					PLAY_CTRL_RESTORE = 6;  //  恢复播放
					PLAY_CTRL_JUMPTIME = 7;  //  跳转到当前曲 pos 秒处位置
	 * @param pos   cmd = 2:  跳转的歌曲序号     cmd = 7:  跳转的曲目时间位置
	 * @return
	 *       返回：  TRUE:  成功   FALSE:  失败
	 */
	public boolean IPCAST_FilePlayCtrl(int sid, int cmd, int pos);    
	/**
	 * 获取服务器文件总数
	 */
	public int IPCAST_FileGetListAll(int[]p,int nSize);
    /**
     * 获取服务器文件信息
     */
	public boolean IPCAST_FileGetInfo(int fid,FileAttr.ByReference fildInfo);
	
	/**
	 * 回调指针接口 
	 */
	public interface IPCastCallBack extends StdCallCallback{
		public int invoke(int EventNo,String ParamStr);
	}
	
	/**
	 * 设置回调指针
	 * @param pFunc
	 */
	public void   IPCAST_SetCallBack(IPCastCallBack pFunc);
	
	/**
	 * 发起对讲
	 */
	public boolean IPCAST_Start_Talk(CallAddr.ByReference from,CallAddr.ByReference target);
	
	/**
	 * 控制接通
	 */
	public boolean IPCAST_Accept_Call(int tid);
	/**
	 * 挂断对讲
	 */
	public boolean IPCAST_Stop_Speech(int tid);

	/**
	 * 文件上传
	 */
	//public int IPCAST_UploadFile(UploadFileAttr.ByReference fileInfo,int[] handle);
	//public int IPCAST_UploadFile(byte[] fileName,byte[] mediaName,int parentId,boolean bPrivate,int[] handle);
	public int IPCAST_UploadFileEx(String fileName,String mediaName,int parentId,boolean bPrivate,int[] handle);
	/**
	 * 获取文件上传进度
	 */
	public int IPCAST_GetUploadFileSizeEx(int handle,int[]size,int[]totalSize);
}
