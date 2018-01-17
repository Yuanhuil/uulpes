

function getAuthObject()
{
	var objAuth = "";
	if(window.ActiveXObject || "ActiveXObject" in window) //IE
	{
		objAuth = document.getElementById("AuthIE");
	}
	else  
	{
		objAuth = document.getElementById("AuthNoIE");
	}
	return objAuth;
}


/**********************************************************************************************
Function: reportStatus
Parameters: status
Return: Description
Description: Report status's description.
***********************************************************************************************/
function reportStatus(status)
{
	var text = "Unknown status code";
	switch (status)
	{
	case 0:     text = "Success";
		break;
	case 1:     text = "Request exceeds data file range";
		break;
	case 3:     text = "System is out of memory";
		break;
	case 4:     text = "Too many open login sessions";
		break;
	case 5:     text = "Access denied";
		break;
	case 7:     text = "Required SuperDog not found";
		break;
	case 8:     text = "Encryption/decryption data length is too short";
		break;
	case 9:     text = "Invalid input handle";
		break;
	case 10:    text = "Specified File ID not recognized by API";
		break;
	case 15:    text = "Invalid XML format";
		break;
	case 18:    text = "SuperDog to be updated not found";
		break;
	case 19:    text = "Invalid update data";
		break;
	case 20:    text = "Update not supported by SuperDog";
		break;
	case 21:    text = "Update counter is set incorrectly";
		break;
	case 22:    text = "Invalid Vendor Code passed";
		break;
	case 24:    text = "Passed time value is outside supported value range";
		break;
	case 26:    text = "Acknowledge data requested by the update, however the ack_data input parameter is NULL";
		break;
	case 27:    text = "Program running on a terminal server";
		break;
	case 29:    text = "Unknown algorithm used in V2C file";
		break;
	case 30:    text = "Signature verification failed";
		break;
	case 31:    text = "Requested Feature not available";
		break;
	case 33:    text = "Communication error between API and local SuperDog License Manager";
		break;
	case 34:    text = "Vendor Code not recognized by API";
		break;
	case 35:    text = "Invalid XML specification";
		break;
	case 36:    text = "Invalid XML scope";
		break;
	case 37:    text = "Too many SuperDog currently connected";
		break;
	case 39:    text = "Session was interrupted";
		break;
	case 41:    text = "Feature has expired";
		break;
	case 42:    text = "SuperDog License Manager version too old";
		break;					
	case 43:    text = "USB error occurred when communicating with a SuperDog";
		break;
	case 45:    text = "System time has been tampered with";
		break;
	case 46:    text = "Communication error occurred in secure channel";
		break;
	case 50:    text = "Unable to locate a Feature matching the scope";
		break;
	case 54:    text = "The values of the update counter in the file are lower than those in the SuperDog";
		break;
	case 55:    text = "The first value of the update counter in the file is greater than the value in the SuperDog";
		break;					
	case 400:   text = "Unable to locate dynamic library for API";
		break;
	case 401:   text = "Dynamic library for API is invalid";
		break;
	case 500:   text = "Object incorrectly initialized";
		break;					
	case 501:   text = "Invalid function parameter";
		break;
	case 502:   text = "Logging in twice to the same object";
		break;
	case 503:   text = "Logging out twice of the same object";
		break;
	case 525:   text = "Incorrect use of system or platform";
		break;
	case 698:   text = "Requested function not implemented";
		break;
	case 699:   text = "Internal error occurred in API";
		break;
	case 700:   text = "Password's length should be between 6-16 characters";
		break;
	case 703:   text = "Verify password failed";
		break;
	case 704:   text = "User's password length should be between 6-16 characters";
		break;
	case 705:   text = "Modify user password failed";
		break;	
	case 802:   text = "The parameter is null";
		break;	
	case 803:   text = "The length of authenticate code is not correct";
		break;	
	case 804:   text = "Have not login yet";
		break;	
	case 810:   text = "Password's length is wrong";
		break;	
	case 811:   text = "The length of parameter is not correct";
		break;	
	case 812:   text = "Info's length is wrong";
		break;
	case 813:   text = "Name's length is wrong";
		break;	
	case 814:   text = "The length of authenticate factor is not correct";
		break;
	case 815:   text = "The length of Dog ID is not correct";
		break;
	case 821:   text = "Need to verify SO password";
		break;
	case 822:   text = "Need to verify Password";
		break;
	case 823:   text = "Buffer length is not enough";
		break;	
	case 824:   text = "Need to initialize";
		break;
	case 825:   text = "Password has been locked";
		break;
	case 831:   text = "Verify password failed, you still have 14 chances";
		break;	
	case 832:   text = "Verify password failed, you still have 13 chances";
		break;
	case 833:   text = "Verify password failed, you still have 12 chances";
		break;
	case 834:   text = "Verify password failed, you still have 11 chances";
		break;
	case 835:   text = "Verify password failed, you still have 10 chances";
		break;
	case 836:   text = "Verify password failed, you still have 9 chances";
		break;
	case 837:   text = "Verify password failed, you still have 8 chances";
		break;
	case 838:   text = "Verify password failed, you still have 7 chances";
		break;
	case 839:   text = "Verify password failed, you still have 6 chances";
		break;
	case 840:   text = "Verify password failed, you still have 5 chances";
		break;
	case 841:   text = "Verify password failed, you still have 4 chances";
		break;
	case 842:   text = "Verify password failed, you still have 3 chances";
		break;
	case 843:   text = "Verify password failed, you still have 2 chances";
		break;
	case 844:   text = "Verify password failed, you still have 1 chance";
		break;
	case 845:   text = "Password has been locked";
		break;	
	
	case 901:   text = "Please enter your user name!";
		break;	
	case 902:   text = "Name length should be between 1-32 characters";
		break;	
	case 903:   text = "Name is illegal";
		break;	
	case 904:   text = "Please enter your password";
		break;	
	case 905:   text = "Password's length should be between 6-16 characters";
		break;	
	case 906:   text = "Password is illegal";
		break;	
	case 907:   text = "This password is not safe, please enter another one";
		break;
	case 908:   text = "Please enter your confirm password";
		break;
	case 909:   text = "Confirm password's length should be between 6-16 characters";
		break;	
	case 910:   text = "Password is illegal";
		break;	
	case 911:   text = "Passwords do not match";
		break;
	case 1001:   text = "no dog_auth_srv in java.library.path";
		break;	
	case 1002:   text = "Fail to get challenge";
		break;	
	case 1003:   text = "Fail to get challenge";
		break;		
	case 1020:   text = "The SuperDog has been registered!";
		break;		
	}
	document.getElementById("errorinfo").innerHTML = text + " (" + status + ")\n";
}
