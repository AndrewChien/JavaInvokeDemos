/**
 * 用途：java 简单测试demo
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
	public static IPCastCallBack ipcb;//回调函数
	//初始化API
	public static boolean SDKInit()
	{
		return IPCastSDKObj.IPCAST_SDKInit();
	}
	//释放API
	public static boolean SDKRelease()
	{
		return IPCastSDKObj.IPCAST_SDKRealease();
	}
	//服务器连接
	public static boolean connect(){
		return IPCastSDKObj.IPCAST_Connect(ip, user, pass);
	}
	
	//断开服务器连接
	public static void disConnect(){
		IPCastSDKObj.IPCAST_DisConnect();
	}
	//设置回调函数		
	public static IPCastCallBack getIpcb() {
		return ipcb;
	}
	public static void setIpcb(IPCastCallBack ipcb) {
		Test02.ipcb = ipcb;
	}
    
	/**
     * 播放文件，当播放本地文件fid=0;当播放服务器文件，fid=服务器文件id
     * @return
     */
	public static int playFile(){
		int len=1;
		String path="";
		File directory = new File("");//设定为当前文件夹
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
			pf.fid=427;//如果播放服务器上的文件，把fid改为获取到的服务器文件id
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
	 * 获取服务器文件信息，首先获取文件总数，然后读取文件信息
	 * @param args
	 */
	public static void FileGetInfo()
	{
		int[]p = null;
		int nSize=0;
		nSize=IPCastSDKObj.IPCAST_FileGetListAll(p,nSize);//获取文件总数
		if(nSize>0)
		{
			p=new int[nSize];
			
			IPCastSDKObj.IPCAST_FileGetListAll(p,nSize);
			IPCastSDK.FileAttr.ByReference fileInfo=new IPCastSDK.FileAttr.ByReference();
			for(int i=0;i<nSize;i++)
			{
				IPCastSDKObj.IPCAST_FileGetInfo(p[i],  fileInfo);
				//将fname转换为string 
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
	 * 终端对讲
	 * @param args
	 */
	public static boolean TalkStatr(int fromTerm,int targetTerm)
	{
		//定义并初始化对讲终端信息
		IPCastSDK.CallAddr.ByReference from=new IPCastSDK.CallAddr.ByReference();
		IPCastSDK.CallAddr.ByReference target=new IPCastSDK.CallAddr.ByReference();
		from.tid=fromTerm;
		from.box_id=0;
		target.tid=targetTerm;
		target.box_id=0;
		return IPCastSDKObj.IPCAST_Start_Talk(from, target);
	}
	/**
	 * 控制终端接通
	 * @param args
	 */
	public static boolean TalkAccept(int tid)
	{
		return IPCastSDKObj.IPCAST_Accept_Call(tid);
	}
	/**
	 * 挂断对讲
	 * @param args
	 */
	public static boolean stopTalk(int tid)
	{
		return IPCastSDKObj.IPCAST_Stop_Speech(tid);
	}
    public static void MainFuncSelect()
    {
    	System.out.println("--------------------------------");
        System.out.println("功能测试选项：");
        System.out.println("1.文件播放  2.终端呼叫  3.获取服务器文件列表  4.退出测试 5 文件上传");
        System.out.println("--------------------------------");
    }
    public static void TalkFuncSelect()
    {
    	System.out.println("--------------------------------");
        System.out.println("功能测试选项：");
        System.out.println("1.接听对接  2.对接挂断  ");
        System.out.println("--------------------------------");
    }
    //文件上传
    public static int upLoadFile()
    {
    	IPCastSDK.UploadFileAttr.ByReference fileInfo=new IPCastSDK.UploadFileAttr.ByReference();    	
    	String localName="E:\\TestMusic\\经典老歌 - 军中绿花.mp3";
    	String mediaName="经典老歌 - 军中绿花.mp3";
    	
    //	fileInfo.localName=localName.getBytes();
    //	fileInfo.mediaName=mediaName.getBytes();
   // 	fileInfo.parantId=0;
   // 	fileInfo.bPrivate=false;
    	
    	int[] handle=new int[1];
    	int sid=-1;
    	System.out.println("=======================执行了=================");
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
	 * main 函数
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	 	boolean flag=true;
		SDKInit();//初始化API接口
		boolean conn=connect();//连接服务器
        if(conn)
        {
    	   System.out.println("connect success");
        }
        else
        {
        	return;//当连接服务器失败，不再调用任何函数
        }
        //设置回调函数
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
	        		//文件播放
	                if(conn){
	                	sessionId=playFile();
	                	System.out.println(sessionId);
	                }
	        		break;
	        	case 2:
	        		 //终端对讲
	        	       boolean talkstate= TalkStatr(1,2);//tid=1-->tid=2
	        	       if(talkstate)
	        	       {
	        	    	   System.out.println("发起呼叫成功");
	        	    	   TalkFuncSelect();
	        	    	   Scanner talk=new Scanner(System.in);
	        	           int func=talk.nextInt();
	        	           switch(func)
	        	           {
	        	           		case 1:
	        	           		 //控制终端接通
	        	           	       if(TalkAccept(2))//tid=2接听
	        	           	       {
	        	           	    	   System.out.println("接听成功");
	        	           	       }
	        	           			break;
	        	           		case 2:
	        	           		 //挂断对讲
	        	           	       if(stopTalk(1))
	        	           	       {
	        	           	    	   System.out.println("挂断成功");
	        	           	       }
	        	           			break;
	        	           		default:
	        	           			System.out.println("输入有误");
	        	           			break;
	        	           }
	        	       }
	        		break;
	        	case 3:
	        		 //获取服务器文件
	                FileGetInfo();
	        		break;
	        	case 4:
	        		flag=false;
	        		break;
	        	case 5:
	        		upLoadFile();
	        		break;
	        	default:
	        		System.out.println("输入有误");
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
