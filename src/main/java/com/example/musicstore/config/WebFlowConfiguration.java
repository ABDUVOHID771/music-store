//package com.example.musicstore.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.webflow.config.AbstractFlowConfiguration;
//import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
//import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
//import org.springframework.webflow.executor.FlowExecutor;
//import org.springframework.webflow.mvc.builder.MvcViewFactoryCreator;
//import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
//import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;
//
//@Configuration
//public class WebFlowConfiguration extends AbstractFlowConfiguration {
//
//    @Bean
//    public FlowDefinitionRegistry flowRegistry() {
//        return getFlowDefinitionRegistryBuilder()
//                .setBasePath("classpath:flows")
//                .addFlowLocation("/checkout/checkout-flow.xml", "checkout")
//                .build();
//    }
//
//    @Bean
//    public FlowExecutor flowExecutor() {
//        return getFlowExecutorBuilder(flowRegistry()).build();
//    }
//
//    @Bean
//    public FlowHandlerMapping flowHandlerMapping() {
//        FlowHandlerMapping handlerMapping = new FlowHandlerMapping();
//        handlerMapping.setOrder(-1);
//        handlerMapping.setFlowRegistry(this.flowRegistry());
//        return handlerMapping;
//    }
//
//    @Bean
//    public FlowHandlerAdapter flowHandlerAdapter() {
//        FlowHandlerAdapter handlerAdapter = new FlowHandlerAdapter();
//        handlerAdapter.setFlowExecutor(this.flowExecutor());
//        handlerAdapter.setSaveOutputToFlashScopeOnRedirect(true);
//        return handlerAdapter;
//    }
//
//    @Bean
//    public FlowBuilderServices flowBuilderServices() {
//        return getFlowBuilderServicesBuilder()
//                .setViewFactoryCreator(new MvcViewFactoryCreator())
//                .setDevelopmentMode(true).build();
//    }
//
//}