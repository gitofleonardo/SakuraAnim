package cn.huangchengxi.imomoe.activities.detail

class PlayPresenter(private val player: PlayModel.Player):PlayModel.Player {
    private val model=PlayModel(this)

    fun getInformation(url:String){
        model.getBasicInformation(url)
    }
    fun getPlayAddress(url:String){
        model.parseStringUrl(url)
    }
    override fun onSuccessParsing(playUrl: String) {
        player.onSuccessParsing(playUrl)
    }

    override fun onSuccessLoadingInformation(information: AnimInformation) {
        player.onSuccessLoadingInformation(information)
    }

    override fun onErrorParsing(errorMsg: String) {

    }

    override fun onNetworkError() {

    }
}