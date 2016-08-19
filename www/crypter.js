var exec = require('cordova/exec');

var Crypter = {
	encrypter: function(input_file, output_file, success, failure) {
        cordova.exec(
            success,
            failure,
            'crypter',
            'encrypt',
            [input_file, output_file]
        );
    },
	decrypter: function(input_file, output_file, success, failure) {
        cordova.exec(
            success,
            failure,
            'crypter',
            'decrypt',
            [input_file, output_file]
        );
    }
	deleteFile: function(input_file, success, failure) {
        cordova.exec(
            success,
            failure,
            'delete',
            'decrypt',
            [input_file]
        );
    }	
};
module.exports = Crypter;