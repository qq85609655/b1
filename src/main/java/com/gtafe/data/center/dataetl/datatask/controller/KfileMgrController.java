package com.gtafe.data.center.dataetl.datatask.controller;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.dataetl.datatask.service.DataTaskService;
import com.gtafe.data.center.dataetl.datatask.vo.KfileParam;
import com.gtafe.data.center.dataetl.datatask.vo.TransFileVo;
import com.gtafe.framework.base.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(path = "/kfilemgr")
@CrossOrigin
public class KfileMgrController extends BaseController {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(KfileMgrController.class);


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
     * @param fileId
     * @return
     */
    @RequestMapping(path = "/runIt", method = RequestMethod.GET)
    public boolean runIt(int fileId) {
        return this.dataTaskServiceImpl.runItem(fileId);
    }


}
