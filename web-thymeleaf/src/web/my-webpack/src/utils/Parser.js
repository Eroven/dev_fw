
export function parseToken(token) {
    try {
        let profile = JSON.parse(decodeURIComponent(escape(window.atob(token.split(".")[1]))))
        return profile
    } catch (e) {
        console.error(e)
    }
}
