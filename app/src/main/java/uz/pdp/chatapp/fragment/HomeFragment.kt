package uz.pdp.chatapp.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import uz.pdp.chatapp.R
import uz.pdp.chatapp.databinding.FragmentHomeBinding
import uz.pdp.chatapp.modles.UserData

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var gso: GoogleSignInOptions
    private lateinit var signInClient: GoogleSignInClient
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    private val RC_SIGN_IN = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference
        val currentUser = firebaseAuth.currentUser
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(binding.root.context, gso)
        firebaseDatabase = FirebaseDatabase.getInstance()
        if (currentUser != null) {
            findNavController().popBackStack()
            findNavController().navigate(R.id.pagerFragment)
        }
        binding.signGoogle.setOnClickListener {
            signIn()
        }
        binding.longOut.setOnClickListener {
            firebaseAuth.signOut()
        }
        return binding.root
    }

    private fun signIn() {
        val signInIntent: Intent = signInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account?.idToken!!)
            }
        } else {
            Toast.makeText(binding.root.context, "Is do not", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity(), OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = firebaseAuth.currentUser!!
                    val uid = user.uid
                    val username = user.displayName
                    val imageUrl = user.photoUrl.toString()
                    val telNumber = user.phoneNumber
                    val email = user.email
                    val userData =
                        UserData(uid, username, telNumber, imageUrl, email, null, null, true)
                    reference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val children = snapshot.children
                            var b = false
                            for (child in children) {
                                val value = child.getValue(UserData::class.java)
                                if (value != null && value.getUid() == user.uid) {
                                    b = true
                                    break
                                }
                            }
                            if (!b) {
                                reference.child("Users").child(user.uid).setValue(userData)
                            }
                            findNavController().popBackStack()
                            findNavController().navigate(R.id.pagerFragment)
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            })
    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = firebaseAuth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
    }
}