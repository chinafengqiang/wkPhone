package xy.com.utils;

import java.util.HashMap;
import java.util.List;


/**
 * 
 * @author FQ.CHINA
 *
 */
public class ObjUtils {
  
  //判断map中存放的对象是否存在
  public static boolean isNotBlankI(HashMap<String, Object> map,String key){
      if(map == null)
        return false;
      Object value = map.get(key);
      if(value == null)
        return false;
      if((Integer)value <= 0)
        return false;
      return true;
  }
  
  public static boolean isBlankI(HashMap<String, Object> map,String key){
    if(map == null)
      return true;
    Object value = map.get(key);
    if(value == null)
      return true;
    if((Integer)value <= 0)
      return true;
    return false;
}
  
  public static boolean isNotBlankI(HashMap<String, Object> map){
    return isNotBlankI(map, "ID");
  }
  
  public static boolean isBlankI(HashMap<String, Object> map){
    return isBlankI(map, "ID");
  }
  
  //判断map中存放的对象是否存在
  public static boolean isNotBlankL(HashMap<String, Object> map,String key){
      if(map == null)
        return false;
      Object value = map.get(key);
      if(value == null)
        return false;
      if((Long)value <= 0)
        return false;
      return true;
  }
  
  public static boolean isNotBlankL(HashMap<String, Object> map){
    return isNotBlankL(map, "ID");
  }
  
  public static boolean isListBlank(List<HashMap<String, Object>> resList){
    if(resList == null || resList.size() <= 0)
      return true;
    else
      return false;
  }

  public static boolean isListNotBlank(List<HashMap<String, Object>> resList){
      if(resList != null && resList.size() > 0)
        return true;
      else
        return false;
  }
}
