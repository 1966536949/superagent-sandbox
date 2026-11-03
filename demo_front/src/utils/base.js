const base = {
    get() {
        return {
            url: "http://localhost:8080/vote/",
            name: "vote",
            // 退出到首页链接
            indexUrl: ''
        };
    },
    getProjectName() {
        return {
            projectName: "在线投票管理系统"
        }
    }
}
export default base
