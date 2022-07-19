package it.polito.wa2.login_service.services


import it.polito.wa2.login_service.repositories.ActivationRepository
import it.polito.wa2.login_service.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class DBCleaningServiceImpl: DBCleaningService {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var activationRepository: ActivationRepository

    //Start after 3 days and clean every hour
    //@Scheduled(initialDelayString = "PT10S", fixedRateString = "PT10S") //useful for debugging
    @Scheduled(initialDelayString = "P3D", fixedRateString = "PT1H")
    @Throws(InterruptedException::class)
    override fun cleanDB(){
        println("Cleaning...")
        val nowDate = LocalDate.now()
        // it's no longer necessary to delete activations (because of ON DELETE CASCADE)
        activationRepository.getExpiredActivations(nowDate)?.forEach { activation ->
            //activationRepository.deleteActivationByProvisionalId(activation.provisionalId)
            userRepository.deleteById(activation.user!!.id)
        }
    }
}
