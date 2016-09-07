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
    },
    encrypterDoc: function(input_file, output_file, success, failure) {
        cordova.exec(
            success,
            failure,
            'crypter',
            'encrypt_doc',
            [input_file, output_file]
        );
    },
    decrypterDoc: function(input_file, output_file, success, failure) {
        cordova.exec(
            success,
            failure,
            'crypter',
            'decrypt_doc',
            [input_file, output_file]
        );
    },

    deleteFile: function(input_file, success, failure) {
        cordova.exec(
            success,
            failure,
            'crypter',
            'delete',
            [input_file]
        );
    }
};
module.exports = Crypter;