const menu = {
    list() {
        return [{
            "backMenu": [{
                "child": [{
                    "appFrontIcon": "cuIcon-addressbook",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "公告信息",
                    "menuJump": "列表",
                    "tableName": "gonggaoxinxi"
                }], "menu": "公告信息管理"
            }, {
                "child": [{
                    "appFrontIcon": "cuIcon-shop",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "用户",
                    "menuJump": "列表",
                    "tableName": "yonghu"
                }], "menu": "用户管理"
            }, {
                "child": [{
                    "appFrontIcon": "cuIcon-addressbook",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "投票分类",
                    "menuJump": "列表",
                    "tableName": "toupiaofenlei"
                }], "menu": "投票分类管理"
            }, {
                "child": [{
                    "appFrontIcon": "cuIcon-discover",
                    "buttons": ["新增", "查看", "修改", "删除", "报表"],
                    "menu": "投票活动",
                    "menuJump": "列表",
                    "tableName": "toupiaohuodong"
                }], "menu": "投票活动管理"
            }, {
                "child": [{
                    "appFrontIcon": "cuIcon-newshot",
                    "buttons": ["查看", "修改", "删除", "报表"],
                    "menu": "投票记录",
                    "menuJump": "列表",
                    "tableName": "toupiaojilu"
                }], "menu": "投票记录管理"
            }],
            "frontMenu": [],
            "hasBackLogin": "是",
            "hasBackRegister": "否",
            "hasFrontLogin": "否",
            "hasFrontRegister": "否",
            "roleName": "管理员",
            "tableName": "users"
        }, {
            "backMenu": [{
                "child": [{
                    "appFrontIcon": "cuIcon-addressbook",
                    "buttons": ["查看"],
                    "menu": "公告信息",
                    "menuJump": "列表",
                    "tableName": "gonggaoxinxi"
                }], "menu": "公告信息管理"
            }, {
                "child": [{
                    "appFrontIcon": "cuIcon-discover",
                    "buttons": ["查看", "投票"],
                    "menu": "投票活动",
                    "menuJump": "列表",
                    "tableName": "toupiaohuodong"
                }], "menu": "投票活动管理"
            }, {
                "child": [{
                    "appFrontIcon": "cuIcon-newshot",
                    "buttons": ["查看", "报表"],
                    "menu": "投票记录",
                    "menuJump": "列表",
                    "tableName": "toupiaojilu"
                }], "menu": "投票记录管理"
            }],
            "frontMenu": [],
            "hasBackLogin": "是",
            "hasBackRegister": "是",
            "hasFrontLogin": "否",
            "hasFrontRegister": "否",
            "roleName": "用户",
            "tableName": "yonghu"
        }]
    }
}
export default menu;
