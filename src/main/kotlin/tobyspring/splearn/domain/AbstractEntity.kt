package tobyspring.splearn.domain

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.proxy.HibernateProxy
import java.util.Objects

@MappedSuperclass
abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val oEffectiveClass = if (other is HibernateProxy) {
            other.hibernateLazyInitializer.persistentClass
        } else {
            other::class.java
        }

        val thisEffectiveClass = if (this is HibernateProxy) {
            (this as HibernateProxy).hibernateLazyInitializer.persistentClass
        } else {
            this::class.java
        }

        if (thisEffectiveClass != oEffectiveClass) return false

        other as AbstractEntity
        return id != 0L && Objects.equals(id, other.id)
    }

    override fun hashCode(): Int {
        return if (this is HibernateProxy) {
            this.hibernateLazyInitializer.persistentClass.hashCode()
        } else {
            this::class.java.hashCode()
        }
    }
}
