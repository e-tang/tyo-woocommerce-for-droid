const fs = require('fs');

var file = fs.readFileSync(process.argv[2], "UTF-8");

var lines = file.split('\n');

for (var i = 2; i < lines.length; ++i) {
    var line = lines[i];
    line = line.trim();

    if (line.length == 0)
        continue;

    var id = 1000 + i;
    var cols = line.split(',');
    
    var buffer = '';
    //for (var j = 0; j < cols.length; j++) {
        buffer += '';
        buffer += ',simple';
        buffer += ',' + cols[0];
        buffer += ',' + cols[0];
        buffer += ',1,1,visible';
        buffer += ',' + cols[1];
        buffer += ',' + cols[2] + '; ' +cols[3] + '; ' + cols[4];
        buffer += ',,,taxable,,1,,0,0,,,,,1,,55,65,"Auto Parts",,,,,,,,,,,,,,,';
    //}

    // for (var k = )
    console.log(buffer);

    //console.log(line);
}
