package com.gtafe.data.center.dataetl.datatask.controller;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.dataetl.datatask.service.DataTaskService;
import com.gtafe.data.center.dataetl.datatask.vo.KfileParam;
import com.gtafe.data.center.dataetl.datatask.vo.TransFileVo;
import com.gtafe.data.center.system.config.mapper.SysConfigMapper;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/kfilemgr")
@CrossOrigin
public class KfileMgrController extends BaseController {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(KfileMgrController.class);


    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Resource
    private DataTaskService dataTaskServiceImpl;

    @RequestMapping(path = "/queryLocalTransFileList", method = RequestMethod.POST)
    public @ResponseBody
    PageInfo<TransFileVo> queryLocalTransFileList(@RequestBody KfileParam param, @RequestParam(value = "fileType", required = true) String fileType,
                                                  @RequestParam(value = "fileName", required = true) String fileName) {

        param.setFileName(fileName);
        param.setFileType(fileType);
        List<TransFileVo> result = dataTaskServiceImpl.queryKfileList(param.getFileType(),
                param.getFileName(), param.getPageNum(), param.getPageSize());
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<TransFileVo>(result);
    }

    /**
     * 手动执行ktr任务
     *
     * @param fileName
     * @return
     */
    @RequestMapping(path = "/runIt/{fileType}/{fileName}", method = RequestMethod.GET)
    public boolean runIt(@PathVariable("fileType") String fileType, @PathVariable("fileName") String fileName) {
        String[] v = fileName.split(",");
        List<String> vlist = new ArrayList<String>();
        for (String mdddp : v) {
            vlist.add(mdddp);
        }
        return this.dataTaskServiceImpl.runItem(vlist, fileType);
    }

    @RequestMapping(path = "/load/{fileType}", method = RequestMethod.GET)
    public boolean load(@PathVariable("fileType") String fileType) {
        String filePath = "";
        SysConfigVo sysConfigVo = sysConfigMapper.queryEntity(false);
        if (sysConfigVo != null) {
            if (fileType.equals("ktr")) {
                filePath = sysConfigVo.getKtrFilesPath();
            } else {
                filePath = sysConfigVo.getKjbFilesPath();
            }
        }
        dataTaskServiceImpl.flushTransFileVo(filePath, fileType);
        System.out.println("扫描" + fileType + "文件目录" + filePath + "操作结束");

        return true;
    }

    @RequestMapping(path = "/downIt", method = RequestMethod.GET)
    public String template(String filePath) {
        System.out.println(filePath);
        return "forward:" + filePath;
    }

    @RequestMapping(path = "/sendInTask", method = RequestMethod.GET)
    public boolean sendInTask(String filePath) {
        return this.dataTaskServiceImpl.sendInTask(filePath);
    }

}
