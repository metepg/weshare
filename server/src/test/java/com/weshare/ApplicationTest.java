package com.weshare;

import com.weshare.service.BillService;
import com.weshare.service.CategoryService;
import com.weshare.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends TestcontainersConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @DisplayName("Services exist")
    @Test
    void applicationContextExist() {
        assertThat(applicationContext.getBean(BillService.class)).isNotNull();
        assertThat(applicationContext.getBean(CategoryService.class)).isNotNull();
        assertThat(applicationContext.getBean(UserService.class)).isNotNull();
    }
}
