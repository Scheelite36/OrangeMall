package com.orange.orangemall.method;

import com.orange.orangemall.enums.SatusEnum;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Scheelite
 * @date 2021/10/24
 * @email jwei.gan@qq.com
 * @description
 **/
public class EnumTest {

    @Test
    public void showEnum(){
        for(SatusEnum s:SatusEnum.values()){
            System.out.println(s.name());
        }
    }
}
