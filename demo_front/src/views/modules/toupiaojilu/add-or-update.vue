<template>
  <div class="addEdit-block">
    <el-form
        ref="ruleForm"
        :model="ruleForm"
        :rules="rules"
        :style="{backgroundColor:addEditForm.addEditBoxColor}"
        class="detail-form-content"
        label-width="80px"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item v-if="type!='info'" class="input" label="投票名称" prop="toupiaomingcheng">
            <el-input v-model="ruleForm.toupiaomingcheng"
                      :readonly="ro.toupiaomingcheng" clearable placeholder="投票名称"></el-input>
          </el-form-item>
          <div v-else>
            <el-form-item class="input" label="投票名称" prop="toupiaomingcheng">
              <el-input v-model="ruleForm.toupiaomingcheng"
                        placeholder="投票名称" readonly></el-input>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="type!='info'" class="input" label="投票分类" prop="toupiaofenlei">
            <el-input v-model="ruleForm.toupiaofenlei"
                      :readonly="ro.toupiaofenlei" clearable placeholder="投票分类"></el-input>
          </el-form-item>
          <div v-else>
            <el-form-item class="input" label="投票分类" prop="toupiaofenlei">
              <el-input v-model="ruleForm.toupiaofenlei"
                        placeholder="投票分类" readonly></el-input>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="type!='info'" class="select" label="票数" prop="piaoshu">
            <el-select v-model="ruleForm.piaoshu" :disabled="ro.piaoshu" placeholder="请选择票数">
              <el-option
                  v-for="(item,index) in piaoshuOptions"
                  v-bind:key="index"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <div v-else>
            <el-form-item class="input" label="票数" prop="piaoshu">
              <el-input v-model="ruleForm.piaoshu"
                        placeholder="票数" readonly></el-input>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="type!='info'" class="date" label="投票时间" prop="toupiaoshijian">
            <el-date-picker
                v-model="ruleForm.toupiaoshijian"
                :readonly="ro.toupiaoshijian"
                placeholder="投票时间"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
          </el-form-item>
          <div v-else>
            <el-form-item v-if="ruleForm.toupiaoshijian" class="input" label="投票时间" prop="toupiaoshijian">
              <el-input v-model="ruleForm.toupiaoshijian"
                        placeholder="投票时间" readonly></el-input>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="type!='info'" class="input" label="用户名" prop="yonghuming">
            <el-input v-model="ruleForm.yonghuming"
                      :readonly="ro.yonghuming" clearable placeholder="用户名"></el-input>
          </el-form-item>
          <div v-else>
            <el-form-item class="input" label="用户名" prop="yonghuming">
              <el-input v-model="ruleForm.yonghuming"
                        placeholder="用户名" readonly></el-input>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="type!='info'" class="input" label="手机" prop="shouji">
            <el-input v-model="ruleForm.shouji"
                      :readonly="ro.shouji" clearable placeholder="手机"></el-input>
          </el-form-item>
          <div v-else>
            <el-form-item class="input" label="手机" prop="shouji">
              <el-input v-model="ruleForm.shouji"
                        placeholder="手机" readonly></el-input>
            </el-form-item>
          </div>
        </el-col>
      </el-row>
      <el-form-item class="btn">
        <el-button v-if="type!='info'" class="btn-success" type="primary" @click="onSubmit">提交</el-button>
        <el-button v-if="type!='info'" class="btn-close" @click="back()">取消</el-button>
        <el-button v-if="type=='info'" class="btn-close" @click="back()">返回</el-button>
      </el-form-item>
    </el-form>


  </div>
</template>
<script>
// 数字，邮件，手机，url，身份证校验
import {checkIdCard, isEmail, isIntNumer, isMobile, isNumber, isPhone, isURL} from "@/utils/validate";

export default {
  data() {
    let self = this
    var validateIdCard = (rule, value, callback) => {
      if (!value) {
        callback();
      } else if (!checkIdCard(value)) {
        callback(new Error("请输入正确的身份证号码"));
      } else {
        callback();
      }
    };
    var validateUrl = (rule, value, callback) => {
      if (!value) {
        callback();
      } else if (!isURL(value)) {
        callback(new Error("请输入正确的URL地址"));
      } else {
        callback();
      }
    };
    var validateMobile = (rule, value, callback) => {
      if (!value) {
        callback();
      } else if (!isMobile(value)) {
        callback(new Error("请输入正确的手机号码"));
      } else {
        callback();
      }
    };
    var validatePhone = (rule, value, callback) => {
      if (!value) {
        callback();
      } else if (!isPhone(value)) {
        callback(new Error("请输入正确的电话号码"));
      } else {
        callback();
      }
    };
    var validateEmail = (rule, value, callback) => {
      if (!value) {
        callback();
      } else if (!isEmail(value)) {
        callback(new Error("请输入正确的邮箱地址"));
      } else {
        callback();
      }
    };
    var validateNumber = (rule, value, callback) => {
      if (!value) {
        callback();
      } else if (!isNumber(value)) {
        callback(new Error("请输入数字"));
      } else {
        callback();
      }
    };
    var validateIntNumber = (rule, value, callback) => {
      if (!value) {
        callback();
      } else if (!isIntNumer(value)) {
        callback(new Error("请输入整数"));
      } else {
        callback();
      }
    };
    return {
      addEditForm: {
        "btnSaveFontColor": "rgba(37, 35, 35, 0.68)",
        "selectFontSize": "14px",
        "btnCancelBorderColor": "rgba(238, 237, 229, 0.33)",
        "inputBorderRadius": "20px",
        "inputFontSize": "14px",
        "textareaBgColor": "#fff",
        "btnSaveFontSize": "14px",
        "textareaBorderRadius": "20px",
        "uploadBgColor": "#fff",
        "textareaBorderStyle": "solid",
        "btnCancelWidth": "88px",
        "textareaHeight": "120px",
        "dateBgColor": "#fff",
        "btnSaveBorderRadius": "20px",
        "uploadLableFontSize": "14px",
        "textareaBorderWidth": "1px",
        "inputLableColor": "rgba(52, 51, 50, 0.88)",
        "addEditBoxColor": "rgba(232, 212, 212, 0.24)",
        "dateIconFontSize": "14px",
        "btnSaveBgColor": "#409EFF",
        "uploadIconFontColor": "#8c939d",
        "textareaBorderColor": "#DCDFE6",
        "btnCancelBgColor": "rgba(231, 242, 112, 0.92)",
        "selectLableColor": "#606266",
        "btnSaveBorderStyle": "solid",
        "dateBorderWidth": "1px",
        "dateLableFontSize": "14px",
        "dateBorderRadius": "20px",
        "btnCancelBorderStyle": "solid",
        "selectLableFontSize": "14px",
        "selectBorderStyle": "solid",
        "selectIconFontColor": "#C0C4CC",
        "btnCancelHeight": "44px",
        "inputHeight": "40px",
        "btnCancelFontColor": "#606266",
        "dateBorderColor": "#DCDFE6",
        "dateIconFontColor": "#C0C4CC",
        "uploadBorderStyle": "solid",
        "dateBorderStyle": "solid",
        "dateLableColor": "#606266",
        "dateFontSize": "14px",
        "inputBorderWidth": "1px",
        "uploadIconFontSize": "28px",
        "selectHeight": "40px",
        "inputFontColor": "#606266",
        "uploadHeight": "148px",
        "textareaLableColor": "#606266",
        "textareaLableFontSize": "14px",
        "btnCancelFontSize": "14px",
        "inputBorderStyle": "solid",
        "btnCancelBorderRadius": "20px",
        "inputBgColor": "#fff",
        "inputLableFontSize": "14px",
        "uploadLableColor": "#606266",
        "uploadBorderRadius": "20px",
        "btnSaveHeight": "44px",
        "selectBgColor": "#fff",
        "btnSaveWidth": "88px",
        "selectIconFontSize": "14px",
        "dateHeight": "40px",
        "selectBorderColor": "#DCDFE6",
        "inputBorderColor": "#DCDFE6",
        "uploadBorderColor": "#DCDFE6",
        "textareaFontColor": "#606266",
        "selectBorderWidth": "1px",
        "dateFontColor": "rgba(159, 111, 85, 0.88)",
        "btnCancelBorderWidth": "1px",
        "uploadBorderWidth": "1px",
        "textareaFontSize": "14px",
        "selectBorderRadius": "20px",
        "selectFontColor": "#606266",
        "btnSaveBorderColor": "#409EFF",
        "btnSaveBorderWidth": "1px"
      },
      id: '',
      type: '',
      ro: {
        toupiaomingcheng: false,
        toupiaofenlei: false,
        piaoshu: false,
        toupiaoshijian: false,
        yonghuming: false,
        shouji: false,
        crossuserid: false,
        crossrefid: false,
      },
      ruleForm: {
        toupiaomingcheng: '',
        toupiaofenlei: '',
        piaoshu: '',
        toupiaoshijian: '',
        yonghuming: '',
        shouji: '',
        crossuserid: '',
        crossrefid: '',
      },
      piaoshuOptions: [],
      rules: {
        toupiaomingcheng: [],
        toupiaofenlei: [],
        piaoshu: [
          {required: true, message: '票数不能为空', trigger: 'blur'},
          {validator: validateIntNumber, trigger: 'blur'},
        ],
        toupiaoshijian: [],
        yonghuming: [],
        shouji: [
          {validator: validateMobile, trigger: 'blur'},
        ],
        crossuserid: [],
        crossrefid: [],
      }
    };
  },
  props: ["parent"],
  computed: {},
  created() {
    this.ruleForm.toupiaoshijian = this.getCurDateTime()

    this.addEditStyleChange()
    this.addEditUploadStyleChange()
  },
  methods: {
    // 下载
    download(file) {
      window.open(`${file}`)
    },
    // 初始化
    init(id, type) {
      if (id) {
        this.id = id;
        this.type = type;
      }
      if (this.type == 'info' || this.type == 'else') {
        this.info(id);
      } else if (this.type == 'logistics') {
        this.logistics = false;
        this.info(id);
      } else if (this.type == 'cross') {
        var obj = this.$storage.getObj('crossObj');
        for (var o in obj) {
          if (o == 'toupiaomingcheng') {
            this.ruleForm.toupiaomingcheng = obj[o];
            this.ro.toupiaomingcheng = true;
            continue;
          }
          if (o == 'toupiaofenlei') {
            this.ruleForm.toupiaofenlei = obj[o];
            this.ro.toupiaofenlei = true;
            continue;
          }
          if (o == 'piaoshu') {
            this.ruleForm.piaoshu = obj[o];
            this.ro.piaoshu = true;
            continue;
          }
          if (o == 'toupiaoshijian') {
            this.ruleForm.toupiaoshijian = obj[o];
            this.ro.toupiaoshijian = true;
            continue;
          }
          if (o == 'yonghuming') {
            this.ruleForm.yonghuming = obj[o];
            this.ro.yonghuming = true;
            continue;
          }
          if (o == 'shouji') {
            this.ruleForm.shouji = obj[o];
            this.ro.shouji = true;
            continue;
          }
          if (o == 'crossuserid') {
            this.ruleForm.crossuserid = obj[o];
            this.ro.crossuserid = true;
            continue;
          }
          if (o == 'crossrefid') {
            this.ruleForm.crossrefid = obj[o];
            this.ro.crossrefid = true;
            continue;
          }
        }
        this.ruleForm.piaoshu = 0
        this.ro.piaoshu = false;
      }
      // 获取用户信息
      this.$http({
        url: `${this.$storage.get('sessionTable')}/session`,
        method: "get"
      }).then(({data}) => {
        if (data && data.code === 0) {
          var json = data.data;
          if (this.$storage.get("role") != "管理员") {
            this.ro.toupiaomingcheng = true;
          }
          if (this.$storage.get("role") != "管理员") {
            this.ro.toupiaofenlei = true;
          }
          if (json.yonghuming != '' && json.yonghuming) {
            this.ruleForm.yonghuming = json.yonghuming
            this.ro.yonghuming = true;
          }
          if (json.shouji != '' && json.shouji) {
            this.ruleForm.shouji = json.shouji
            this.ro.shouji = true;
          }
        } else {
          this.$message.error(data.msg);
        }
      });
      this.piaoshuOptions = "1".split(',')
    },
    // 多级联动参数
    info(id) {
      this.$http({
        url: `toupiaojilu/info/${id}`,
        method: "get"
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.ruleForm = data.data;
          //解决前台上传图片后台不显示的问题
          let reg = new RegExp('../../../upload', 'g')//g代表全部
        } else {
          this.$message.error(data.msg);
        }
      });
    },


    // 提交
    onSubmit() {


      var objcross = this.$storage.getObj('crossObj');
      var table = this.$storage.getObj('crossTable');
      if (objcross != null) {
        objcross.piaoshu = parseFloat(objcross.piaoshu) + parseFloat(this.ruleForm.piaoshu)
      }
      this.$http({
        url: `${table}/update`,
        method: "post",
        data: objcross
      }).then(({data}) => {
      });

      //更新跨表属性
      var crossuserid;
      var crossrefid;
      var crossoptnum;
      if (this.type == 'cross') {
        var statusColumnName = this.$storage.get('statusColumnName');
        var statusColumnValue = this.$storage.get('statusColumnValue');
        if (statusColumnName != '') {
          var obj = this.$storage.getObj('crossObj');
          if (!statusColumnName.startsWith("[")) {
            for (var o in obj) {
              if (o == statusColumnName) {
                obj[o] = statusColumnValue;
              }
            }
            var table = this.$storage.get('crossTable');
            this.$http({
              url: `${table}/update`,
              method: "post",
              data: obj
            }).then(({data}) => {
            });
          } else {
            crossuserid = this.$storage.get('userid');
            crossrefid = obj['id'];
            crossoptnum = this.$storage.get('statusColumnName');
            crossoptnum = crossoptnum.replace(/\[/, "").replace(/\]/, "");
          }
        }
      }
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          if (crossrefid && crossuserid) {
            this.ruleForm.crossuserid = crossuserid;
            this.ruleForm.crossrefid = crossrefid;
            let params = {
              page: 1,
              limit: 10,
              crossuserid: this.ruleForm.crossuserid,
              crossrefid: this.ruleForm.crossrefid,
            }
            this.$http({
              url: "toupiaojilu/page",
              method: "get",
              params: params
            }).then(({
                       data
                     }) => {
              if (data && data.code === 0) {
                if (data.data.total >= crossoptnum) {
                  this.$message.error(this.$storage.get('tips'));
                  return false;
                } else {
                  this.$http({
                    url: `toupiaojilu/${!this.ruleForm.id ? "save" : "update"}`,
                    method: "post",
                    data: this.ruleForm
                  }).then(({data}) => {
                    if (data && data.code === 0) {
                      this.$message({
                        message: "操作成功",
                        type: "success",
                        duration: 1500,
                        onClose: () => {
                          this.parent.showFlag = true;
                          this.parent.addOrUpdateFlag = false;
                          this.parent.toupiaojiluCrossAddOrUpdateFlag = false;
                          this.parent.search();
                          this.parent.contentStyleChange();
                        }
                      });
                    } else {
                      this.$message.error(data.msg);
                    }
                  });

                }
              } else {
              }
            });
          } else {
            this.$http({
              url: `toupiaojilu/${!this.ruleForm.id ? "save" : "update"}`,
              method: "post",
              data: this.ruleForm
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: "操作成功",
                  type: "success",
                  duration: 1500,
                  onClose: () => {
                    this.parent.showFlag = true;
                    this.parent.addOrUpdateFlag = false;
                    this.parent.toupiaojiluCrossAddOrUpdateFlag = false;
                    this.parent.search();
                    this.parent.contentStyleChange();
                  }
                });
              } else {
                this.$message.error(data.msg);
              }
            });
          }
        }
      });
    },
    // 获取uuid
    getUUID() {
      return new Date().getTime();
    },
    // 返回
    back() {
      this.parent.showFlag = true;
      this.parent.addOrUpdateFlag = false;
      this.parent.toupiaojiluCrossAddOrUpdateFlag = false;
      this.parent.contentStyleChange();
    },
    addEditStyleChange() {
      this.$nextTick(() => {
        // input
        document.querySelectorAll('.addEdit-block .input .el-input__inner').forEach(el => {
          el.style.height = this.addEditForm.inputHeight
          el.style.color = this.addEditForm.inputFontColor
          el.style.fontSize = this.addEditForm.inputFontSize
          el.style.borderWidth = this.addEditForm.inputBorderWidth
          el.style.borderStyle = this.addEditForm.inputBorderStyle
          el.style.borderColor = this.addEditForm.inputBorderColor
          el.style.borderRadius = this.addEditForm.inputBorderRadius
          el.style.backgroundColor = this.addEditForm.inputBgColor
        })
        document.querySelectorAll('.addEdit-block .input .el-form-item__label').forEach(el => {
          el.style.lineHeight = this.addEditForm.inputHeight
          el.style.color = this.addEditForm.inputLableColor
          el.style.fontSize = this.addEditForm.inputLableFontSize
        })
        // select
        document.querySelectorAll('.addEdit-block .select .el-input__inner').forEach(el => {
          el.style.height = this.addEditForm.selectHeight
          el.style.color = this.addEditForm.selectFontColor
          el.style.fontSize = this.addEditForm.selectFontSize
          el.style.borderWidth = this.addEditForm.selectBorderWidth
          el.style.borderStyle = this.addEditForm.selectBorderStyle
          el.style.borderColor = this.addEditForm.selectBorderColor
          el.style.borderRadius = this.addEditForm.selectBorderRadius
          el.style.backgroundColor = this.addEditForm.selectBgColor
        })
        document.querySelectorAll('.addEdit-block .select .el-form-item__label').forEach(el => {
          el.style.lineHeight = this.addEditForm.selectHeight
          el.style.color = this.addEditForm.selectLableColor
          el.style.fontSize = this.addEditForm.selectLableFontSize
        })
        document.querySelectorAll('.addEdit-block .select .el-select__caret').forEach(el => {
          el.style.color = this.addEditForm.selectIconFontColor
          el.style.fontSize = this.addEditForm.selectIconFontSize
        })
        // date
        document.querySelectorAll('.addEdit-block .date .el-input__inner').forEach(el => {
          el.style.height = this.addEditForm.dateHeight
          el.style.color = this.addEditForm.dateFontColor
          el.style.fontSize = this.addEditForm.dateFontSize
          el.style.borderWidth = this.addEditForm.dateBorderWidth
          el.style.borderStyle = this.addEditForm.dateBorderStyle
          el.style.borderColor = this.addEditForm.dateBorderColor
          el.style.borderRadius = this.addEditForm.dateBorderRadius
          el.style.backgroundColor = this.addEditForm.dateBgColor
        })
        document.querySelectorAll('.addEdit-block .date .el-form-item__label').forEach(el => {
          el.style.lineHeight = this.addEditForm.dateHeight
          el.style.color = this.addEditForm.dateLableColor
          el.style.fontSize = this.addEditForm.dateLableFontSize
        })
        document.querySelectorAll('.addEdit-block .date .el-input__icon').forEach(el => {
          el.style.color = this.addEditForm.dateIconFontColor
          el.style.fontSize = this.addEditForm.dateIconFontSize
          el.style.lineHeight = this.addEditForm.dateHeight
        })
        // upload
        let iconLineHeight = parseInt(this.addEditForm.uploadHeight) - parseInt(this.addEditForm.uploadBorderWidth) * 2 + 'px'
        document.querySelectorAll('.addEdit-block .upload .el-upload--picture-card').forEach(el => {
          el.style.width = this.addEditForm.uploadHeight
          el.style.height = this.addEditForm.uploadHeight
          el.style.borderWidth = this.addEditForm.uploadBorderWidth
          el.style.borderStyle = this.addEditForm.uploadBorderStyle
          el.style.borderColor = this.addEditForm.uploadBorderColor
          el.style.borderRadius = this.addEditForm.uploadBorderRadius
          el.style.backgroundColor = this.addEditForm.uploadBgColor
        })
        document.querySelectorAll('.addEdit-block .upload .el-form-item__label').forEach(el => {
          el.style.lineHeight = this.addEditForm.uploadHeight
          el.style.color = this.addEditForm.uploadLableColor
          el.style.fontSize = this.addEditForm.uploadLableFontSize
        })
        document.querySelectorAll('.addEdit-block .upload .el-icon-plus').forEach(el => {
          el.style.color = this.addEditForm.uploadIconFontColor
          el.style.fontSize = this.addEditForm.uploadIconFontSize
          el.style.lineHeight = iconLineHeight
          el.style.display = 'block'
        })
        // 多文本输入框
        document.querySelectorAll('.addEdit-block .textarea .el-textarea__inner').forEach(el => {
          el.style.height = this.addEditForm.textareaHeight
          el.style.color = this.addEditForm.textareaFontColor
          el.style.fontSize = this.addEditForm.textareaFontSize
          el.style.borderWidth = this.addEditForm.textareaBorderWidth
          el.style.borderStyle = this.addEditForm.textareaBorderStyle
          el.style.borderColor = this.addEditForm.textareaBorderColor
          el.style.borderRadius = this.addEditForm.textareaBorderRadius
          el.style.backgroundColor = this.addEditForm.textareaBgColor
        })
        document.querySelectorAll('.addEdit-block .textarea .el-form-item__label').forEach(el => {
          // el.style.lineHeight = this.addEditForm.textareaHeight
          el.style.color = this.addEditForm.textareaLableColor
          el.style.fontSize = this.addEditForm.textareaLableFontSize
        })
        // 保存
        document.querySelectorAll('.addEdit-block .btn .btn-success').forEach(el => {
          el.style.width = this.addEditForm.btnSaveWidth
          el.style.height = this.addEditForm.btnSaveHeight
          el.style.color = this.addEditForm.btnSaveFontColor
          el.style.fontSize = this.addEditForm.btnSaveFontSize
          el.style.borderWidth = this.addEditForm.btnSaveBorderWidth
          el.style.borderStyle = this.addEditForm.btnSaveBorderStyle
          el.style.borderColor = this.addEditForm.btnSaveBorderColor
          el.style.borderRadius = this.addEditForm.btnSaveBorderRadius
          el.style.backgroundColor = this.addEditForm.btnSaveBgColor
        })
        // 返回
        document.querySelectorAll('.addEdit-block .btn .btn-close').forEach(el => {
          el.style.width = this.addEditForm.btnCancelWidth
          el.style.height = this.addEditForm.btnCancelHeight
          el.style.color = this.addEditForm.btnCancelFontColor
          el.style.fontSize = this.addEditForm.btnCancelFontSize
          el.style.borderWidth = this.addEditForm.btnCancelBorderWidth
          el.style.borderStyle = this.addEditForm.btnCancelBorderStyle
          el.style.borderColor = this.addEditForm.btnCancelBorderColor
          el.style.borderRadius = this.addEditForm.btnCancelBorderRadius
          el.style.backgroundColor = this.addEditForm.btnCancelBgColor
        })
      })
    },
    addEditUploadStyleChange() {
      this.$nextTick(() => {
        document.querySelectorAll('.addEdit-block .upload .el-upload-list--picture-card .el-upload-list__item').forEach(el => {
          el.style.width = this.addEditForm.uploadHeight
          el.style.height = this.addEditForm.uploadHeight
          el.style.borderWidth = this.addEditForm.uploadBorderWidth
          el.style.borderStyle = this.addEditForm.uploadBorderStyle
          el.style.borderColor = this.addEditForm.uploadBorderColor
          el.style.borderRadius = this.addEditForm.uploadBorderRadius
          el.style.backgroundColor = this.addEditForm.uploadBgColor
        })
      })
    },
  }
};
</script>
<style lang="scss">
.editor {
  height: 500px;

  & /deep/ .ql-container {
    height: 310px;
  }
}

.amap-wrapper {
  width: 100%;
  height: 500px;
}

.search-box {
  position: absolute;
}

.addEdit-block {
  margin: -10px;
}

.detail-form-content {
  padding: 12px;
  background-color: transparent;
}

.btn .el-button {
  padding: 0;
}
</style>
