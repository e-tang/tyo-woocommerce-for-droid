const fs = require('fs');

var file = fs.readFileSync(process.argv[2], "UTF-8");
var jsonObj = JSON.parse(file),
    typeObj;

if (process.argv.length > 3) {
    var file2 = fs.readFileSync(process.argv[3], "UTF-8");
    typeObj = JSON.parse(file2);
}

var targetObj = {};

for (var key in jsonObj) {
    targetObj[key] = {};

    // i18n
    targetObj[key]['i18n'] = {};
    targetObj[key]['i18n']['en'] = jsonObj[key];
    targetObj[key]['i18n']['cn'] = '';

    // value type
    if (typeObj && typeObj[key])
        targetObj[key]['type'] = typeObj[key];
    else
        targetObj[key]['type'] = '';

    targetObj[key]['visible'] = 'false';
}

console.log(JSON.stringify(targetObj));
