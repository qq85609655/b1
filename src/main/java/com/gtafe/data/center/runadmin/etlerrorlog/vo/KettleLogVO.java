package com.gtafe.data.center.runadmin.etlerrorlog.vo;

import com.gtafe.framework.base.vo.BaseVO;

/**
 * 任务层级的错误日志记录
 */
public class KettleLogVO extends BaseVO {

    /**
     * 意义，目的和功能，以及被用到的地方<br>
     */
    private static final long serialVersionUID = 7191964703887001539L;
    private int id_batch;

    private String channel_id;

    private String transname;

    private String status;

    private int lines_read;

    private int lines_written;

    private int line_updated;

    private int line_input;

    private int line_output;

    private int line_rejected;

    private int errors;

    private String startdate;

    private String enddate;

    private String logdate;

    private String depdate;

    private String repaydate;

    private String log_field;

    private String executing_server;

    private String executing_user;

    private String client;

    private int orgId;

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    private String orgName;

    public int getId_batch() {
        return id_batch;
    }

    public void setId_batch(int id_batch) {
        this.id_batch = id_batch;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getTransname() {
        return transname;
    }

    public void setTransname(String transname) {
        this.transname = transname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLines_read() {
        return lines_read;
    }

    public void setLines_read(int lines_read) {
        this.lines_read = lines_read;
    }

    public int getLines_written() {
        return lines_written;
    }

    public void setLines_written(int lines_written) {
        this.lines_written = lines_written;
    }

    public int getLine_updated() {
        return line_updated;
    }

    public void setLine_updated(int line_updated) {
        this.line_updated = line_updated;
    }

    public int getLine_input() {
        return line_input;
    }

    public void setLine_input(int line_input) {
        this.line_input = line_input;
    }

    public int getLine_output() {
        return line_output;
    }

    public void setLine_output(int line_output) {
        this.line_output = line_output;
    }

    public int getLine_rejected() {
        return line_rejected;
    }

    public void setLine_rejected(int line_rejected) {
        this.line_rejected = line_rejected;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getLogdate() {
        return logdate;
    }

    public void setLogdate(String logdate) {
        this.logdate = logdate;
    }

    public String getDepdate() {
        return depdate;
    }

    public void setDepdate(String depdate) {
        this.depdate = depdate;
    }

    public String getRepaydate() {
        return repaydate;
    }

    public void setRepaydate(String repaydate) {
        this.repaydate = repaydate;
    }

    public String getLog_field() {
        return log_field;
    }

    public void setLog_field(String log_field) {
        this.log_field = log_field;
    }

    public String getExecuting_server() {
        return executing_server;
    }

    public void setExecuting_server(String executing_server) {
        this.executing_server = executing_server;
    }

    public String getExecuting_user() {
        return executing_user;
    }

    public void setExecuting_user(String executing_user) {
        this.executing_user = executing_user;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "KettleLogVO [id_batch=" + id_batch + ", channel_id="
                + channel_id + ", transname=" + transname + ", status=" + status
                + ", lines_read=" + lines_read + ", lines_written="
                + lines_written + ", line_updated=" + line_updated
                + ", line_input=" + line_input + ", line_output=" + line_output
                + ", line_rejected=" + line_rejected + ", errors=" + errors
                + ", startdate=" + startdate + ", enddate=" + enddate
                + ", logdate=" + logdate + ", depdate=" + depdate
                + ", repaydate=" + repaydate + ", log_field=" + log_field
                + ", executing_server=" + executing_server + ", executing_user="
                + executing_user + ", client=" + client + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((channel_id == null) ? 0 : channel_id.hashCode());
        result = prime * result + ((client == null) ? 0 : client.hashCode());
        result = prime * result + ((depdate == null) ? 0 : depdate.hashCode());
        result = prime * result + ((enddate == null) ? 0 : enddate.hashCode());
        result = prime * result + errors;
        result = prime * result
                + ((executing_server == null) ? 0 : executing_server.hashCode());
        result = prime * result
                + ((executing_user == null) ? 0 : executing_user.hashCode());
        result = prime * result + id_batch;
        result = prime * result + line_input;
        result = prime * result + line_output;
        result = prime * result + line_rejected;
        result = prime * result + line_updated;
        result = prime * result + lines_read;
        result = prime * result + lines_written;
        result = prime * result
                + ((log_field == null) ? 0 : log_field.hashCode());
        result = prime * result + ((logdate == null) ? 0 : logdate.hashCode());
        result = prime * result
                + ((repaydate == null) ? 0 : repaydate.hashCode());
        result = prime * result
                + ((startdate == null) ? 0 : startdate.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result
                + ((transname == null) ? 0 : transname.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KettleLogVO other = (KettleLogVO) obj;
        if (channel_id == null) {
            if (other.channel_id != null)
                return false;
        } else if (!channel_id.equals(other.channel_id))
            return false;
        if (client == null) {
            if (other.client != null)
                return false;
        } else if (!client.equals(other.client))
            return false;
        if (depdate == null) {
            if (other.depdate != null)
                return false;
        } else if (!depdate.equals(other.depdate))
            return false;
        if (enddate == null) {
            if (other.enddate != null)
                return false;
        } else if (!enddate.equals(other.enddate))
            return false;
        if (errors != other.errors)
            return false;
        if (executing_server == null) {
            if (other.executing_server != null)
                return false;
        } else if (!executing_server.equals(other.executing_server))
            return false;
        if (executing_user == null) {
            if (other.executing_user != null)
                return false;
        } else if (!executing_user.equals(other.executing_user))
            return false;
        if (id_batch != other.id_batch)
            return false;
        if (line_input != other.line_input)
            return false;
        if (line_output != other.line_output)
            return false;
        if (line_rejected != other.line_rejected)
            return false;
        if (line_updated != other.line_updated)
            return false;
        if (lines_read != other.lines_read)
            return false;
        if (lines_written != other.lines_written)
            return false;
        if (log_field == null) {
            if (other.log_field != null)
                return false;
        } else if (!log_field.equals(other.log_field))
            return false;
        if (logdate == null) {
            if (other.logdate != null)
                return false;
        } else if (!logdate.equals(other.logdate))
            return false;
        if (repaydate == null) {
            if (other.repaydate != null)
                return false;
        } else if (!repaydate.equals(other.repaydate))
            return false;
        if (startdate == null) {
            if (other.startdate != null)
                return false;
        } else if (!startdate.equals(other.startdate))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (transname == null) {
            if (other.transname != null)
                return false;
        } else if (!transname.equals(other.transname))
            return false;
        return true;
    }

}
