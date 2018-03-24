package org.allen.imocker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.controller.request.CreateApiInfoRequest;
import org.allen.imocker.controller.request.QueryApiInfoRequest;
import org.allen.imocker.controller.request.UpdateApiInfoRequest;
import org.allen.imocker.controller.vo.ApiConditionVo;
import org.allen.imocker.controller.vo.ApiInfoVo;
import org.allen.imocker.dto.*;
import org.allen.imocker.entity.ApiCondition;
import org.allen.imocker.entity.ApiDoc;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.service.ApiDocService;
import org.allen.imocker.service.ApiInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

@Controller
@RequestMapping("/manage")
@Slf4j
public class ApiAdminController {




}
