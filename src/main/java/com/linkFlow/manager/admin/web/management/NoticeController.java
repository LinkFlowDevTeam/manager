package com.linkFlow.manager.admin.web.management;

import com.linkFlow.manager.common.config.CommonConstants;
import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.NoticeVO;
import com.linkFlow.manager.common.service.NoticeService;
import com.linkFlow.manager.common.util.CustomComponentUtil;
import com.test32.common.axisj.AxisjGridData;
import com.test32.common.axisj.AxisjGridDataConverter;
import com.test32.common.config.ServerConfigAssistantUtil;
import com.test32.common.paging.PagingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Map;

@Controller
@RequestMapping(value = "/management/notice")
public class NoticeController
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private NoticeService noticeService;
    @Autowired private ConfigDataManager configDataManager;
    @Autowired private CustomComponentUtil customComponentUtil;

    final String FIXED_STYLE_STRING = "<style> img { display: block; max-width: 100%; height: auto; } </style>";

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model)
    {
        return "/management/notice/noticeList";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AxisjGridData getListForAxisjGrid(@RequestParam(value = "pageNo", required = true) String pageNo, @RequestParam(value = "pageSize", required = true) String pageSize, @RequestParam Map<String, Object> paramsMap)
    {
        paramsMap.put("orderColumn", "nt_idx");
        paramsMap.put("order", "DESC");
        PagingData pagingData = noticeService.selectPagingBySearch(paramsMap);
        return AxisjGridDataConverter.Convert(pagingData);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model)
    {
        NoticeVO noticeVO = new NoticeVO();
        model.addAttribute(noticeVO);
        model.addAttribute("pageMode", "create");
        return "/management/notice/noticeWrite";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute NoticeVO noticeVO)
    {
        String content = noticeVO.getNtMessage();
        if(StringUtils.isEmpty(content))
            content = "";

        if( ! content.contains(FIXED_STYLE_STRING))
        {
            content = FIXED_STYLE_STRING + content;
            noticeVO.setNtMessage(content);
        }

        noticeService.insert(noticeVO);

        return "redirect:/management/notice";
    }

    @RequestMapping(value = "/info/{idx}", method = RequestMethod.GET)
    public String info(@PathVariable(value = "idx") Long idx, Model model)
    {
        NoticeVO noticeVO = noticeService.select(idx);
        model.addAttribute(noticeVO);
        model.addAttribute("pageMode", "info");
        return "management/notice/mNoticeInfo";
    }

    @RequestMapping(value = "/modify/{idx}", method = RequestMethod.GET)
    public String modify(@PathVariable(value = "idx") Long idx, Model model)
    {
        NoticeVO noticeVO = noticeService.select(idx);
        model.addAttribute(noticeVO);
        model.addAttribute("pageMode", "modify");
        return "/management/notice/noticeModify";
    }

    @RequestMapping(value = "/modify/{ntIdx}", method = RequestMethod.POST)
    public String modify(@PathVariable(value = "ntIdx") Long ntIdx, @ModelAttribute @Valid NoticeVO noticeVO)
    {
        String content = noticeVO.getNtMessage();
        if( ! StringUtils.isEmpty(content))
        {
            if( ! content.contains(FIXED_STYLE_STRING))
            {
                content = FIXED_STYLE_STRING + content;
                noticeVO.setNtMessage(content);
            }
        }
        noticeService.update(noticeVO);
        return "redirect:/management/notice";
    }

    @RequestMapping(value = "/delete/{ntIdx}", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse delete(@PathVariable(value = "ntIdx") Long ntIdx)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        try
        {
            noticeService.delete(ntIdx);
            returnCode = ReturnCode.SUCCESS;
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }


    @RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
    public void upload(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile upload)
    {
        OutputStream out = null;
        PrintWriter printWriter = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        try {
            String fileName = upload.getOriginalFilename();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String[] nameSplit = fileName.split("\\.");
            String postfix = nameSplit[nameSplit.length-1];
            fileName = dateFormat.format(customComponentUtil.getDatabaseNow()) + "." + postfix;

            //Boolean mimeTypeCheck = Boolean.FALSE;
            byte[] bytes = upload.getBytes();

            String noticePath = customComponentUtil.getUploadPathForNotice();
            ServerConfigAssistantUtil.createPath(noticePath);

            String uploadPath = noticePath + fileName;
            System.out.println("uploadPath : " + uploadPath);
            out = new FileOutputStream(new File(uploadPath));
            out.write(bytes);

            String fileUrl = "/" + fileName;
            System.out.println("fileName : " + fileName);
            System.out.println("fileUrl : " + fileUrl);

            printWriter = response.getWriter();

            String releasePath = configDataManager.getConfigData().getDefaultServerConfigData().getReleaseFilePath() + CommonConstants.FILE_PATH_NOTICE + fileName;
            String responseString = "{ \"uploaded\": true, \"url\": \"REPLACE_PATH\" }\n";
            responseString = responseString.replace("REPLACE_PATH", releasePath);
            printWriter.println(responseString);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
                if (printWriter != null)
                    printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public String preview(@Valid NoticeVO noticeVO, Model model)
    {
        model.addAttribute("noticeVO", noticeVO);

        return "management/notice/mPreview";
    }
}