import cn.hutool.core.util.StrUtil;
import org.junit.Test;

import java.util.List;

public class StrUtilSplitTest {
//    @Test
    public void test(){
        String ids = "1,2,3,4,56";
        List<String> split = StrUtil.split(ids, ',');
        System.out.println(split);
    }
}
