package service;

import entity.Regex;
import entity.Workspace;

import java.util.List;

/**
 * Regex的业务逻辑接口
 */
public interface RegexDAO {

    //查询Workspace的资料
    public List<Regex> queryAllRegex();

    //根据workspace id查询信息
    public Regex queryRegexByWid(int id);

    //添加workspace信息
    public boolean addRegex(Regex regex);

    //修改Workspace信息
    public boolean updateRegex(Regex regex);

    //删除workspace信息
    public boolean deleteRegex(int id);
}
