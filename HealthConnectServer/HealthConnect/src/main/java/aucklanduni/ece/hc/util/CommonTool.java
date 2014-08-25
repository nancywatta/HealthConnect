package aucklanduni.ece.hc.util;

import java.util.Date;
import java.util.GregorianCalendar;
/**
 * 
* @ClassName: CommonTool 
* @Description: 
* @author Zhao Yuan
* @date 2014年8月10日 下午7:44:56 
*
 */
public class CommonTool {

	public static String dateConvert(long dateValue){
		Date dat=new Date(dateValue);  
	    GregorianCalendar gc = new GregorianCalendar();   
	    gc.setTime(dat);  
	    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
	    String sb=format.format(gc.getTime());  
	    return sb;
	    
	}

	public static String getDiffTime(long dateValueLeft,long dateValueRight){
		long diffvalue = dateValueLeft - dateValueRight;

		return ""+Math.round(diffvalue/1000/60);
	}
	
	public static void main(String[] args){
//		long sd=1408239856032L;
			  //1407464040000
//		System.out.println(dateConvert(System.currentTimeMillis()));
		System.out.println(dateConvert(1408240259544L));
		System.out.println(dateConvert(1408240261948L));
		
//		System.out.println(getDiffTime(1407554956000L,System.currentTimeMillis()));
	}
}
