/**
 * ��;��java �򵥲���demo
 */
package test;


import ipcast.IPCastSDK;
import ipcast.IPCastSDK.IPCastCallBack;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;


public class Test02 {

	private static String ip="172.16.11.224";
	private static String user="admin";
	private static String pass="admin";
	static IPCastSDK IPCastSDKObj = IPCastSDK.INSTANCE;
	private static int sessionId=0;
	public static IPCastCallBack ipcb;//�ص�����
	//��ʼ��API
	public static boolean SDKInit()
	{
		return IPCastSDKObj.IPCAST_SDKInit();
	}
	//�ͷ�API
	public static boolean SDKRelease()
	{
		return IPCastSDKObj.IPCAST_SDKRealease();
	}
	//����������
	public static boolean connect(){
		return IPCastSDKObj.IPCAST_Connect(ip, user, pass);
	}
	
	//�Ͽ�����������
	public static void disConnect(){
		IPCastSDKObj.IPCAST_DisConnect();
	}
	//���ûص�����		
	public static IPCastCallBack getIpcb() {
		return ipcb;
	}
	public static void setIpcb(IPCastCallBack ipcb) {
		Test02.ipcb = ipcb;
	}
    
	/**
     * �����ļ��������ű����ļ�fid=0;�����ŷ������ļ���fid=�������ļ�id
     * @return
     */
	public static int playFile(){
		int len=1;
		String path="";
		File directory = new File("");//�趨Ϊ��ǰ�ļ���
		try{
		   path=directory.getAbsolutePath()+"\\mp3";
		}
		catch(Exception e){} 
		IPCastSDK.PlayFile.ByReference pfslist[]=new IPCastSDK.PlayFile.ByReference[len];
		for(int i=0;i<len;i++){ 
			String fn=path+"\\Test.mp3";
			if(i==1) fn=path+"\\dingdong.mp3";  
			if(i==2) fn=path+"\\holdon.mp3"; 
			IPCastSDK.PlayFile.ByReference pf=new IPCastSDK.PlayFile.ByReference();
			try{
				pf.fname=fn.getBytes("gbk");
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
			pf.fid=427;//������ŷ������ϵ��ļ�����fid��Ϊ��ȡ���ķ������ļ�id
			pf.fvol=10;
			pf.write();
			pfslist[i]=pf;
			pf=null;
		}
		int [] pTList={1};
		int sid=-1;
		sid=IPCastSDKObj.IPCAST_FilePlayStart( pfslist,pfslist.length,pTList,pTList.length,500,IPCastSDK.PLAY_CYC_ALLCIRCLE,0,0);
		return sid;
	}
		
	/**
	 * ��ȡ�������ļ���Ϣ�����Ȼ�ȡ�ļ�������Ȼ���ȡ�ļ���Ϣ
	 * @param args
	 */
	public static void FileGetInfo()
	{
		int[]p = null;
		int nSize=0;
		nSize=IPCastSDKObj.IPCAST_FileGetListAll(p,nSize);//��ȡ�ļ�����
		if(nSize>0)
		{
			p=new int[nSize];
			
			IPCastSDKObj.IPCAST_FileGetListAll(p,nSize);
			IPCastSDK.FileAttr.ByReference fileInfo=new IPCastSDK.FileAttr.ByReference();
			for(int i=0;i<nSize;i++)
			{
				IPCastSDKObj.IPCAST_FileGetInfo(p[i],  fileInfo);
				//��fnameת��Ϊstring 
				String name="";
				try{
					name=new String(fileInfo.fname,"gbk");
				}
				catch(UnsupportedEncodingException e)
				{
					
				}
				String str="fid:"+fileInfo.fid+" "+"attr:"+fileInfo.attr+" "+"length:"+fileInfo.length+" "+"name:"+name;
				System.out.println(str);
			}
		}
	}
	/**
	 * �ն˶Խ�
	 * @param args
	 */
	public static boolean TalkStatr(int fromTerm,int targetTerm)
	{
		//���岢��ʼ���Խ��ն���Ϣ
		IPCastSDK.CallAddr.ByReference from=new IPCastSDK.CallAddr.ByReference();
		IPCastSDK.CallAddr.ByReference target=new IPCastSDK.CallAddr.ByReference();
		from.tid=fromTerm;
		from.box_id=0;
		target.tid=targetTerm;
		target.box_id=0;
		return IPCastSDKObj.IPCAST_Start_Talk(from, target);
	}
	/**
	 * �����ն˽�ͨ
	 * @param args
	 */
	public static boolean TalkAccept(int tid)
	{
		return IPCastSDKObj.IPCAST_Accept_Call(tid);
	}
	/**
	 * �Ҷ϶Խ�
	 * @param args
	 */
	public static boolean stopTalk(int tid)
	{
		return IPCastSDKObj.IPCAST_Stop_Speech(tid);
	}
    public static void MainFuncSelect()
    {
    	System.out.println("--------------------------------");
        System.out.println("���ܲ���ѡ�");
        System.out.println("1.�ļ�����  2.�ն˺���  3.��ȡ�������ļ��б�  4.�˳����� 5 �ļ��ϴ�");
        System.out.println("--------------------------------");
    }
    public static void TalkFuncSelect()
    {
    	System.out.println("--------------------------------");
        System.out.println("���ܲ���ѡ�");
        System.out.println("1.�����Խ�  2.�ԽӹҶ�  ");
        System.out.println("--------------------------------");
    }
    //�ļ��ϴ�
    public static int upLoadFile()
    {
    	IPCastSDK.UploadFileAttr.ByReference fileInfo=new IPCastSDK.UploadFileAttr.ByReference();    	
    	String localName="E:\\TestMusic\\�����ϸ� - �����̻�.mp3";
    	String mediaName="�����ϸ� - �����̻�.mp3";
    	
    //	fileInfo.localName=localName.getBytes();
    //	fileInfo.mediaName=mediaName.getBytes();
   // 	fileInfo.parantId=0;
   // 	fileInfo.bPrivate=false;
    	
    	int[] handle=new int[1];
    	int sid=-1;
    	System.out.println("=======================ִ����=================");
    	try {
    		// sid=IPCastSDKObj.IPCAST_UploadFile(fileInfo,handle);
    		sid=IPCastSDKObj.IPCAST_UploadFileEx(localName,mediaName,0,false,handle);
		} catch (Exception e) {
			e.printStackTrace();
		}
    
     System.out.println("uploadFile=========================:"+sid);
     return sid;
    }
	
	/**
	 * main ����
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	 	boolean flag=true;
		SDKInit();//��ʼ��API�ӿ�
		boolean conn=connect();//���ӷ�����
        if(conn)
        {
    	   System.out.println("connect success");
        }
        else
        {
        	return;//�����ӷ�����ʧ�ܣ����ٵ����κκ���
        }
        //���ûص�����
        setIpcb(new IPCastCallBack() {  
		@Override
		public int invoke(int EventNo, String ParamStr) {
				System.out.println(EventNo+"  "+ParamStr);
				return 0;
			}  
        });  
        IPCastSDKObj.IPCAST_SetCallBack(ipcb);
        do
        {
	        MainFuncSelect();
	        Scanner sc=new Scanner(System.in);
	        int select=sc.nextInt();
	        switch( select)
	        {
	        	case 1:
	        		//�ļ�����
	                if(conn){
	                	sessionId=playFile();
	                	System.out.println(sessionId);
	                }
	        		break;
	        	case 2:
	        		 //�ն˶Խ�
	        	       boolean talkstate= TalkStatr(1,2);//tid=1-->tid=2
	        	       if(talkstate)
	        	       {
	        	    	   System.out.println("������гɹ�");
	        	    	   TalkFuncSelect();
	        	    	   Scanner talk=new Scanner(System.in);
	        	           int func=talk.nextInt();
	        	           switch(func)
	        	           {
	        	           		case 1:
	        	           		 //�����ն˽�ͨ
	        	           	       if(TalkAccept(2))//tid=2����
	        	           	       {
	        	           	    	   System.out.println("�����ɹ�");
	        	           	       }
	        	           			break;
	        	           		case 2:
	        	           		 //�Ҷ϶Խ�
	        	           	       if(stopTalk(1))
	        	           	       {
	        	           	    	   System.out.println("�Ҷϳɹ�");
	        	           	       }
	        	           			break;
	        	           		default:
	        	           			System.out.println("��������");
	        	           			break;
	        	           }
	        	       }
	        		break;
	        	case 3:
	        		 //��ȡ�������ļ�
	                FileGetInfo();
	        		break;
	        	case 4:
	        		flag=false;
	        		break;
	        	case 5:
	        		upLoadFile();
	        		break;
	        	default:
	        		System.out.println("��������");
	        		break;
	        }
	      
        }while(flag);
     
      
      
       
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }
	}

}
