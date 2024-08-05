import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
    url: 'http://localhost:8080/',
    realm: 'OurKnowledge',
    clientId: 'ourknowledge-client'
});

export default keycloak;