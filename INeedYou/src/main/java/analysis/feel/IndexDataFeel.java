package analysis.feel;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by huangming on 2016/6/25.
 */
public interface IndexDataFeel {
    /**
     * 指标感知，对一些指标变化做出处理.
     *
     * @param map
     */
    public void feel(Map<String, BigDecimal> map);
}
