
const Base64 = require('js-base64').Base64;

export function parseToken(token) {
    try {
        let payload = token.split(".")[1]
        let decode = Base64.decode(payload)
        let profile = JSON.parse(decode)
        return profile
    } catch (e) {
        console.error(e)
    }
}
