const { spawn } = require('child_process');

const child_1 = spawn('java', ['-jar', 'usr/java-server/target/preview-mpp-java-server.jar']);

const child_2 = spawn('node', ['usr/table-service/src/index.js']);


child_1.stdout.on('data', (data) => {
    console.log("java-server|" + data.toString());
});

child_1.stderr.on('data', (err) => {
    console.error(err.toString());
});

child_1.on('exit', (code) => {
    console.log(`Child 1 exited with code ${code}`);
    child_2.stdin.pause();
    child_2.kill();

});


child_2.stdout.on('data', (data) => {
    console.log("table-service|" + data.toString());
});

child_2.stderr.on('data', (err) => {
    console.error(err.toString()); 
});

child_2.on('exit', (code) => {
    console.log(`Child 2 exited with code ${code}`);
    child_1.stdin.pause();
    child_1.kill();
});
