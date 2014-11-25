import Foundation

// 位置監聽
@objc(LocationService) class LocationService : CDVPlugin {
	
	// 啟動監聽
	func startService(command: CDVInvokedUrlCommand) {
			// 這邊是js會帶進來的參數有用到的再用
	        //  'arrived_title': arrived_title,		//推播的title
            //  'arrived_message': arrived_message,	//推播的內容
            //  'start_title': start_title,			//啟動監聽時推播的title
            //  'start_message': start_message,		//啟動監聽時推播的內容
            //  'latitude': latitude,				//目的地的latitude
            //  'longitude': longitude,				//目的地的longitude
            //  'check_interval': check_interval,	//監聽頻率
            //  'arrived_range': arrived_range		//與目的地距離多少就觸發
	}
	
	// 停止監聽
	func stopService(command: CDVInvokedUrlCommand) {
			//這邊就到達目的地就停止監聽吧
	}
}

