/**
 * ��;��jna ����DLL����
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
	 * �������ţ���ֻ����һ�Σ�
	 */
	public static final int PLAY_CYC_DAN = 0x0001;    //  �������ţ���ֻ����һ�Σ�
	public static final int PLAY_CYC_DANCIRCLE = 0x0002;  //  ����ѭ�����ţ�ѭ������һ����Ŀ��
	public static final int PLAY_CYC_DANORDE = 0x0003;  //  ˳�򲥷ţ����򲥷�ȫ������һ�Σ�
	public static final int PLAY_CYC_ALLCIRCLE = 0x0004;  //  ѭ�����ţ�ѭ���������и�����
	
	public static final int PLAY_CTRL_STOP = 1;    //  ֹͣ�㲥
	public static final int PLAY_CTRL_JUMPFILE = 2;  //  ��ת���� pos ������
	public static final int PLAY_CTRL_NEXT = 3;    //  ������һ������
	public static final int PLAY_CTRL_PREV = 4;    //  ������һ������
	public static final int PLAY_CTRL_PAUSE = 5;    //  ��ͣ����
	public static final int PLAY_CTRL_RESTORE = 6;  //  �ָ�����
	public static final int PLAY_CTRL_JUMPTIME = 7;  //  ��ת����ǰ�� pos �봦λ��
	
	public static final int MAX_NAME=256;
	
	
	/**
	 * �����ļ��ṹ��
	 */
	public static class PlayFile extends Structure
	{
		public static class ByReference extends IPCastSDK.PlayFile implements Structure.ByReference {}  
	    public static class ByValue extends IPCastSDK.PlayFile implements Structure.ByValue {} 
	    
		public int      fid;            //  �ļ���ţ����С�� 0 ��ȡȫ·����
		public byte[]   fname=new byte[MAX_NAME];    //  �ļ�����ȫ·��
		public int		fvol;					// �ļ������Ŵ���(����10��)
		
		
	}
	/**
	 * �������ļ����Խṹ��
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
	//�ļ��ϴ��ṹ��
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
	 * �Խ�/Ѱ���ṹ��
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
	 * ��ʼ��API
	 */
	public boolean IPCAST_SDKInit();
	/**
	 * �ͷ�API�ӿ�
	 */
	public boolean IPCAST_SDKRealease();
	/**
	 * ��¼������
	 */
	public boolean IPCAST_Connect(String ipAddr, String user, String pwd);
	
	/**
	 * �˳���¼
	 */
	public void IPCAST_DisConnect();
	
	
	/**
	 * �����ļ�
	 * @param pfslist     �㲥�ļ��б�
	 * @param fCount     �㲥�ļ�����Ŀ
	 * @param pTList     ��Ҫ��ӵ����ļ��㲥�Ự�Ĺ㲥�ն��б�
	 * @param tCount     ��Ҫ��ӵ��ն���Ŀ
	 * @param Grade      �㲥�ȼ���0~999����ֵԽ�󼶱�Խ�ߣ���������
	 * @param CycMode    ����ģʽ
							PLAY_CYC_DAN = 0x0001;    //  �������ţ���ֻ����һ�Σ�
							PLAY_CYC_DANCIRCLE = 0x0002;  //  ����ѭ�����ţ�ѭ������һ����Ŀ��
							PLAY_CYC_DANORDE = 0x0003;  //  ˳�򲥷ţ����򲥷�ȫ������һ�Σ�
							PLAY_CYC_ALLCIRCLE = 0x0004;  //  ѭ�����ţ�ѭ���������и�����
	 * @param CycCount   ѭ�����Ŵ���������Ҫ��ѭ�����Ŷ��ٴΣ�0����ʾ���޴Σ�
	 * @param CycTime    ����ʱ����ֻ�е� CycCount = 0 ʱ��Ч����λΪ�롣��
	 * @return
	 *    ����:  ���� 0:  ���ع㲥�Ự ID��-1���Ự����ʧ�� 
	 */
	public int IPCAST_FilePlayStart(PlayFile.ByReference[] pFList, int fCount, int [] pTList, int tCount,
			int Grade, int CycMode, int CycCount, int CycTime);


	/**
	 *  �����ļ�����
	 * @param sid   �㲥�Ự ID
	 * @param cmd   ��������
					PLAY_CTRL_STOP = 1;    //  ֹͣ�㲥
					PLAY_CTRL_JUMPFILE = 2;  //  ��ת���� pos ������
					PLAY_CTRL_NEXT = 3;    //  ������һ������
					PLAY_CTRL_PREV = 4;    //  ������һ������
					PLAY_CTRL_PAUSE = 5;    //  ��ͣ����
					PLAY_CTRL_RESTORE = 6;  //  �ָ�����
					PLAY_CTRL_JUMPTIME = 7;  //  ��ת����ǰ�� pos �봦λ��
	 * @param pos   cmd = 2:  ��ת�ĸ������     cmd = 7:  ��ת����Ŀʱ��λ��
	 * @return
	 *       ���أ�  TRUE:  �ɹ�   FALSE:  ʧ��
	 */
	public boolean IPCAST_FilePlayCtrl(int sid, int cmd, int pos);    
	/**
	 * ��ȡ�������ļ�����
	 */
	public int IPCAST_FileGetListAll(int[]p,int nSize);
    /**
     * ��ȡ�������ļ���Ϣ
     */
	public boolean IPCAST_FileGetInfo(int fid,FileAttr.ByReference fildInfo);
	
	/**
	 * �ص�ָ��ӿ� 
	 */
	public interface IPCastCallBack extends StdCallCallback{
		public int invoke(int EventNo,String ParamStr);
	}
	
	/**
	 * ���ûص�ָ��
	 * @param pFunc
	 */
	public void   IPCAST_SetCallBack(IPCastCallBack pFunc);
	
	/**
	 * ����Խ�
	 */
	public boolean IPCAST_Start_Talk(CallAddr.ByReference from,CallAddr.ByReference target);
	
	/**
	 * ���ƽ�ͨ
	 */
	public boolean IPCAST_Accept_Call(int tid);
	/**
	 * �Ҷ϶Խ�
	 */
	public boolean IPCAST_Stop_Speech(int tid);

	/**
	 * �ļ��ϴ�
	 */
	//public int IPCAST_UploadFile(UploadFileAttr.ByReference fileInfo,int[] handle);
	//public int IPCAST_UploadFile(byte[] fileName,byte[] mediaName,int parentId,boolean bPrivate,int[] handle);
	public int IPCAST_UploadFileEx(String fileName,String mediaName,int parentId,boolean bPrivate,int[] handle);
	/**
	 * ��ȡ�ļ��ϴ�����
	 */
	public int IPCAST_GetUploadFileSizeEx(int handle,int[]size,int[]totalSize);
}
