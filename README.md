# Bean Implementation

## Bean 생성

```java
import com.github.ioloolo.annotation.Bean;

@Bean
public class TestBean {

}
```

## Bean 주입

```java
import com.github.ioloolo.annotation.Inject;

public class Main {

	@Inject
	private static TestBean bean;
}
```
