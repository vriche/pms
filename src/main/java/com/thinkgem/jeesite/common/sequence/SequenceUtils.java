package com.thinkgem.jeesite.common.sequence;
import java.util.HashMap;
import java.util.Map;
import java.sql.SQLException;
/**
* Created by IntelliJ IDEA.<br>
* <b>User</b>: leizhimin<br>
* <b>Date</b>: 2008-4-2 15:21:30<br>
* <b>Note</b>: Java实现的Sequence工具
*/
public class SequenceUtils {
    private static SequenceUtils _instance = new SequenceUtils();
    private Map<String, KeyInfo> keyMap = new HashMap<String, KeyInfo>(20); //Sequence载体容器
    private static final int POOL_SIZE = 10;      //Sequence值缓存大小

    /**
     * 禁止外部实例化
     */
    private SequenceUtils() {
    }

    /**
     * 获取SequenceUtils的单例对象
     * @return SequenceUtils的单例对象
     */
    public static SequenceUtils getInstance() {
        return _instance;
    }

    /**
     * 获取下一个Sequence键值
     * @param keyName Sequence名称
     * @return 下一个Sequence键值
     */
    public synchronized long getNextKeyValue(String keyName) {
        KeyInfo keyInfo = null;
        Long keyObject = null;
        try {
            if (keyMap.containsKey(keyName)) {
                keyInfo = keyMap.get(keyName);
            } else {
                keyInfo = new KeyInfo(keyName, POOL_SIZE);
                keyMap.put(keyName, keyInfo);
            }
            keyObject = keyInfo.getNextKey();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keyObject;
    } 
    
}