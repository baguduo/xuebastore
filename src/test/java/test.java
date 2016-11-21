import com.touchfuture.takeout.common.AddressUtil;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

/**
 * Created by user on 2016/11/22.
 */
public class test {
    public static void main(String[] args) throws Exception{
        AddressUtil addressUtils = new AddressUtil();
        // 测试ip 219.136.134.157 中国=华南=广东省=广州市=越秀区=电信
        //125.70.11.136
        //InetAddress.getLocalHost().getHostAddress()
        String ip ="192.168.253.1" ;
        String address = "";
        try {
            address = addressUtils.getAddresses("ip="+ip, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(address);
        // 输出结果为：广东省,广州市,越秀区
    }
}
