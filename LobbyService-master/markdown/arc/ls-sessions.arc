{"createdAt":"2020-11-10T21:14:01.567Z","version":"15.0.7","kind":"ARC#ProjectExport","requests":[{"updated":1605042678172,"created":1601839119516,"headers":"","method":"POST","url":"http://127.0.0.1:4242/api/sessions/4376618515775080881?access_token=/72BVmEq5HlZ01LBrP/Tn/X2PiE=","auth":{"password":"bgp-client-pw","username":"bgp-client-name"},"authType":"basic","requestActions":{"variables":[{"variable":"myVar","value":"","enabled":false}]},"payload":"","description":"","name":"Launch Session","projects":["6184cb0b-06d9-43c3-9498-d6de8723ed87"],"type":"saved","kind":"ARC#RequestData","key":"4521fdc5-79fa-4ad1-bca6-164a9cbb8f55"},{"updated":1605042328386,"created":1601839119516,"headers":"","method":"GET","url":"http://127.0.0.1:4242/api/sessions/4376618515775080881","auth":{"password":"bgp-client-pw","username":"bgp-client-name"},"authType":"basic","requestActions":{"variables":[{"variable":"myVar","value":"","enabled":false}]},"payload":"","description":"","name":"Get Session Details (Specific Session)","projects":["6184cb0b-06d9-43c3-9498-d6de8723ed87"],"type":"saved","kind":"ARC#RequestData","key":"5e232c00-12e1-4af9-9801-ca1566a0c5df"},{"updated":1605042252975,"created":1601839119516,"headers":"","method":"DELETE","url":"http://127.0.0.1:4242/api/sessions/4376618515775080881/players/joerg?access_token=5rvxBlfrxewVzQVxsN%2BJxCLsLgQ=","auth":{"password":"bgp-client-pw","username":"bgp-client-name"},"authType":"basic","requestActions":{"variables":[{"variable":"myVar","value":"","enabled":false}]},"payload":"","description":"","name":"Leave Session","projects":["6184cb0b-06d9-43c3-9498-d6de8723ed87"],"type":"saved","kind":"ARC#RequestData","key":"9ce34b7a-5d80-4a01-aeb8-174ca62aee1e"},{"updated":1605042217870,"created":1601839119516,"headers":"","method":"PUT","url":"http://127.0.0.1:4242/api/sessions/4376618515775080881/players/joerg?access_token=5rvxBlfrxewVzQVxsN%2BJxCLsLgQ=","auth":{"password":"bgp-client-pw","username":"bgp-client-name"},"authType":"basic","requestActions":{"variables":[{"variable":"myVar","value":"","enabled":false}]},"payload":"","description":"","name":"Join Session","projects":["6184cb0b-06d9-43c3-9498-d6de8723ed87"],"type":"saved","kind":"ARC#RequestData","key":"3f0ea76c-c5cc-4930-939c-3d154144a305"},{"updated":1605042101139,"created":1601839119516,"headers":"","method":"DELETE","url":"http://127.0.0.1:4242/api/sessions/4514343469120381886?access_token=/72BVmEq5HlZ01LBrP/Tn/X2PiE=","auth":{"password":"bgp-client-pw","username":"bgp-client-name"},"authType":"basic","requestActions":{"variables":[{"variable":"myVar","value":"","enabled":false}]},"payload":"","description":"","name":"Delete Session","projects":["6184cb0b-06d9-43c3-9498-d6de8723ed87"],"type":"saved","kind":"ARC#RequestData","key":"9e521200-e025-419d-a4d5-d06fd6fd7319"},{"updated":1605041962574,"created":1601839119516,"headers":"Content-Type: application/json","method":"POST","url":"http://127.0.0.1:4242/api/sessions?access_token=/72BVmEq5HlZ01LBrP/Tn/X2PiE=","auth":{"password":"bgp-client-pw","username":"bgp-client-name"},"authType":"basic","requestActions":{"variables":[{"variable":"myVar","value":"","enabled":false}]},"type":"saved","payload":"{\n    \"creator\": \"maex\",\n    \"game\": \"DummyGame1\",\n    \"savegame\": \"\"\n}","name":"Create Session","description":"","projects":["6184cb0b-06d9-43c3-9498-d6de8723ed87"],"kind":"ARC#RequestData","key":"3f5eccee-c74f-4dc8-9fdb-75f6262114ad"},{"updated":1605041480988,"created":1601839946981,"headers":"cookie: JSESSIONID=54B2765BECFB1BBFE5BE8B1442E45782\nauthorization: Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=\nuser-agent: advanced-rest-client\naccept: */*","method":"GET","url":"http://127.0.0.1:4242/api/sessions?hash=fd418abd6bfb85de614f42c50e090d95","auth":{"password":"bgp-client-pw","username":"bgp-client-name"},"authType":"basic","requestActions":{"variables":[{"variable":"myVar","value":"","enabled":false}]},"type":"saved","name":"Get Sessions (Long Poll)","description":"","projects":["6184cb0b-06d9-43c3-9498-d6de8723ed87"],"kind":"ARC#RequestData","key":"bb0bec27-ee78-4a14-adf1-28c4248c85c2"},{"updated":1605041499895,"created":1591452450261,"headers":"","method":"GET","payload":"{\n    \"location\": \"http://127.0.0.1:4243/ColtExpressDemo\",\n    \"maxSessionPlayers\": \"5\",\n    \"minSessionPlayers\": \"3\",\n    \"name\": \"ColtExpressDemo\",\n    \"webSupport\": \"true\"\n}","url":"http://127.0.0.1:4242/api/sessions","auth":{"password":"bgp-client-pw","username":"bgp-client-name"},"authType":"basic","requestActions":{"variables":[{"variable":"myVar","value":"","enabled":false}]},"description":"","name":"Get Sessions (Synchronous)","projects":["6184cb0b-06d9-43c3-9498-d6de8723ed87"],"type":"saved","kind":"ARC#RequestData","key":"dae76f2e-a68b-4b35-b99b-a3c6a502dca1"}],"projects":[{"updated":1605042833133,"order":0,"requests":["dae76f2e-a68b-4b35-b99b-a3c6a502dca1","bb0bec27-ee78-4a14-adf1-28c4248c85c2","3f5eccee-c74f-4dc8-9fdb-75f6262114ad","9e521200-e025-419d-a4d5-d06fd6fd7319","3f0ea76c-c5cc-4930-939c-3d154144a305","9ce34b7a-5d80-4a01-aeb8-174ca62aee1e","5e232c00-12e1-4af9-9801-ca1566a0c5df","4521fdc5-79fa-4ad1-bca6-164a9cbb8f55"],"name":"bgp-sessions","kind":"ARC#ProjectData","key":"6184cb0b-06d9-43c3-9498-d6de8723ed87"}]}