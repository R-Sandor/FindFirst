import useUser from '../lib/useUser'
import Layout from '../components/Layout'
 
const Profile = () => {
  // Fetch the user client-side
  const { user } = useUser({ redirectTo: '/login' })
 
  // Server-render loading state
  if (!user || user.isLoggedIn === false) {
    return <Layout>Loading...</Layout>
  }
 
  // Once the user request finishes, show the user
  return (
    <Layout>
      <h1>Your Profile</h1>
      <pre>{JSON.stringify(user, null, 2)}</pre>
    </Layout>
  )
}
 
export default Profile