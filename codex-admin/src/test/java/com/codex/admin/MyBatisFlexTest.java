package com.codex.admin;

import com.codex.admin.tenant.entity.Tenant;
import com.codex.mapper.CodexMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author evan guo
 */
@SpringBootTest
public class MyBatisFlexTest {

    @Autowired
    private CodexMapper codexMapper;

    @Test
    public void testTenant() {
        List<Tenant> tenants = codexMapper.selectAll(Tenant.class);
        System.out.println(tenants);
    }

}
