package kr.co.permission.ax_permission.model

data class AxPermissionModel(
    val perTitle:String,
    var perContent:String,
    val permission:String,
    var perState:Boolean,
    val perType:String,
    val perIcon:Int
)
