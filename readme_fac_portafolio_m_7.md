# FAC_Portafolio_M7

Proyecto base **Maven multi-módulo** para un portafolio de microservicios.

Incluye 4 módulos vacíos (con test mínimo JUnit 5):
- `usuarios`
- `pagos`
- `inventario`
- `notificaciones`

> **Objetivo**: que cualquiera pueda levantar este esqueleto en **~20 minutos** en Windows usando PowerShell.

---

## 1) Prerrequisitos

- **Java** 17+ (recomendado 21)
- **Maven** 3.8+ (recomendado 3.9.x)
- **Git**

Verifica:
```bash
java -version
mvn -v
git --version
```

---

## 2) Crear el proyecto desde cero (PowerShell)

> Carpeta de trabajo sugerida: `C:\Users\<tu_usuario>\Documents\GitHub`.

```powershell
# Ir a la carpeta de trabajo
Set-Location "$HOME\Documents\GitHub"

# Crear carpeta del proyecto y entrar
New-Item -ItemType Directory -Force -Path "FAC_Portafolio_M7" | Out-Null
Set-Location "FAC_Portafolio_M7"
```

### 2.1 Crear `pom.xml` padre (packaging = pom)

```powershell
@'
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>cl.portafolio.m7</groupId>
  <artifactId>FAC_Portafolio_M7</artifactId>
  <version>1.0.0</version>
  <packaging>pom</packaging>
  <name>FAC Portafolio M7</name>

  <modules>
    <module>usuarios</module>
    <module>pagos</module>
    <module>inventario</module>
    <module>notificaciones</module>
  </modules>

  <properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <junit.version>5.10.0</junit.version>
    <surefire.version>3.2.5</surefire.version>
  </properties>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>${surefire.version}</version>
        <configuration>
          <useModulePath>false</useModulePath>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
'@ | Set-Content -Encoding UTF8 pom.xml
```

### 2.2 Crear módulos + POM hijo + test mínimo

```powershell
foreach ($m in "usuarios","pagos","inventario","notificaciones") {
  New-Item -ItemType Directory -Force -Path "$m\src\main\java" | Out-Null
  New-Item -ItemType Directory -Force -Path "$m\src\test\java" | Out-Null

@'
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>cl.portafolio.m7</groupId>
    <artifactId>FAC_Portafolio_M7</artifactId>
    <version>1.0.0</version>
    <relativePath>../pom.xml</relativePath>
  </parent>

  <artifactId>__MODULE__</artifactId>
  <packaging>jar</packaging>

  <dependencies>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
'@ -replace '__MODULE__',$m | Set-Content -Encoding UTF8 "$m\pom.xml"

@'
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
class SanityTest { @Test void ok(){ assertTrue(true); } }
'@ | Set-Content -Encoding UTF8 "$m\src\test\java\SanityTest.java"
}
```

### 2.3 Crear `.gitignore` y `README.md`

```powershell
@'
target/
*/target/
*.class
*.log
*.iml
.idea/
.vscode/
.DS_Store
Thumbs.db
'@ | Set-Content -Encoding UTF8 .gitignore

@'
# FAC_Portafolio_M7
Proyecto Maven **multi-módulo**:
- usuarios
- pagos
- inventario
- notificaciones

## Build
```bash
mvn clean test
```
'@ | Set-Content -Encoding UTF8 README.md
```

---

## 3) Compilar y ejecutar tests

Desde la **raíz** del proyecto (donde está el `pom.xml` padre):

```powershell
mvn clean test
```

👉 Debes ver un **Reactor** con 5 proyectos y `BUILD SUCCESS`.

> Si aparece “there is no POM in this directory”: estás en la carpeta equivocada. Vuelve a `FAC_Portafolio_M7`.

---

## 4) Publicar en GitHub

1. Crea un **repo vacío** en GitHub: `FAC_Portafolio_M7` (sin README, sin .gitignore, sin license).
2. Copia la URL HTTPS, por ejemplo: `https://github.com/USUARIO/FAC_Portafolio_M7.git`.
3. En PowerShell, ejecuta:

```powershell
git init
git add .
git commit -m "Init: multi-módulo + tests JUnit5 OK"
git branch -M main
git remote add origin https://github.com/USUARIO/FAC_Portafolio_M7.git
git push -u origin main
```

> Si el repo remoto ya tiene commits (por ejemplo, creaste un README en GitHub y olvidaste dejarlo vacío), puedes:
> - Integrar con rebase:
>   ```powershell
>   git pull --rebase origin main
>   git push -u origin main
>   ```
> - O forzar de forma segura (para reemplazar lo remoto con lo local):
>   ```powershell
>   git push -u origin main --force-with-lease
>   ```

---

## 5) Estructura esperada

```
FAC_Portafolio_M7/
├─ pom.xml                    # POM padre (packaging=pom)
├─ usuarios/
│  ├─ pom.xml                 # POM hijo
│  └─ src/test/java/SanityTest.java
├─ pagos/
│  ├─ pom.xml
│  └─ src/test/java/SanityTest.java
├─ inventario/
│  ├─ pom.xml
│  └─ src/test/java/SanityTest.java
├─ notificaciones/
│  ├─ pom.xml
│  └─ src/test/java/SanityTest.java
└─ .gitignore, README.md
```

---

## 6) Errores comunes y soluciones rápidas

- **Non-parseable POM … `xmlns=""http:/` …**  
  El pegado rompió las comillas. Repite los bloques con **here-strings** de comilla simple `@' ... '@`.

- **`there is no POM in this directory`**  
  Estás en la carpeta equivocada. Vuelve a la raíz `FAC_Portafolio_M7`.

- **`push` rechazado: `fetch first / non-fast-forward`**  
  El remoto tiene commits.
  - Integrar: `git pull --rebase origin main` → resolver conflictos → `git push`.
  - Forzar seguro: `git push --force-with-lease`.

- **Rebase “colgado” en Windows** (no borra `.git/rebase-merge`)  
  Borra manualmente la carpeta:
  - Git Bash: `rm -rf .git/rebase-merge`  
  - PowerShell: `Remove-Item -Recurse -Force .git\rebase-merge`

---

## 7) Siguientes pasos (opcionales)

- Convertir `usuarios` en un microservicio **Spring Boot** (`spring-boot-starter-web`) con `/actuator/health`.
- Añadir **GitHub Actions** para CI: workflow con `mvn -B -ntp clean verify`.
- Empaquetar módulos con **Docker** y orquestar con **docker-compose** o **Kubernetes**.

---

## 8) Licencia

Elige una licencia si vas a publicar el proyecto: MIT/Apache-2.0/BSD-3-Clause.

