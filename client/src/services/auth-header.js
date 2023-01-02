export default function authHeader() {
  const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
      return { Authorization: user };
    } else {
      return {};
    }
  }
  