package com.tms.config;

import com.tms.entity.Agreement;
import com.tms.entity.User;
import com.tms.repository.AgreementRepository;
import com.tms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgreementRepository agreementRepository;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有用户数据
        if (userRepository.count() == 0) {
            // 创建默认用户
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("123456"); // 实际项目中应该加密
            admin.setRealName("管理员");
            admin.setPhone("13800138000");
            admin.setEmail("admin@example.com");
            admin.setDepartment("技术部");
            admin.setPosition("管理员");
            userRepository.save(admin);

            User developer = new User();
            developer.setUsername("developer");
            developer.setPassword("123456");
            developer.setRealName("开发者");
            developer.setPhone("13900139000");
            developer.setEmail("developer@example.com");
            developer.setDepartment("技术部");
            developer.setPosition("开发工程师");
            userRepository.save(developer);
        }

        // 检查是否已有协议数据
        if (agreementRepository.count() == 0) {
            User admin = userRepository.findByUsername("admin");
            if (admin != null) {
                Agreement agreement = new Agreement();
                agreement.setTitle("兼职技术人员项目协作协议");
                agreement.setVersion("1.0");
                agreement.setStatus("ACTIVE");
                agreement.setCreator(admin);
                agreement.setContent("## 第一章 总则\n\n第一条 为规范兼职技术人员的工作流程，明确双方权利义务，特制定本协议。\n\n## 第二章 任务领取与开发流程\n\n第二条 兼职技术人员通过任务管理系统领取任务。\n\n## 第三章 报酬计算与支付\n\n第三条 报酬计算以任务复杂度和完成质量为依据。");
                agreementRepository.save(agreement);
            }
        }
    }
}