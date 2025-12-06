# README - Binary Clock Android App

## 📱 Sobre o Projeto

Este repositório contém uma **Prova de Conceito (PoC)** de um aplicativo Android de **Relógio Binário (Binary Clock)**, desenvolvido a partir do [Antigravity](https://github.com/getantibody/antigravity) - um gerador de código inteligente baseado em IA.

O aplicativo implementa um relógio visual inovador que exibe a hora atual em formato binário através de uma grade de círculos interativos e em tempo real.

---

## 🎯 Funcionalidades

- **Exibição em Tempo Real**: Atualiza a hora a cada segundo
- **Grade Binária (6x4)**: 
  - 6 colunas representando: HH:MM:SS (dígito das dezenas e unidades)
  - 4 linhas representando as potências de 2: 8 (2³), 4 (2²), 2 (2¹), 1 (2⁰)
- **Visualização Clara**: 
  - Círculos preenchidos (azuis) = bit 1 (ligado)
  - Círculos vazios (bordas azuis) = bit 0 (desligado)
- **Rótulos Informativos**: 
  - Valores das potências de 2 ao lado das linhas
  - Dígitos decimais atualizados abaixo das colunas

---

## 🛠️ Stack Tecnológico

- **Linguagem**: Kotlin
- **Arquitetura UI**: Jetpack Compose ou XML Layout
- **Versão Mínima**: Android 8.0+ (API 26)
- **Gerenciamento de Tempo**: Handler/CoroutineScope

---

## 📋 Especificações Técnicas

### Lógica de Conversão Binária

O aplicativo realiza, a cada segundo:

1. **Extração de Dígitos**: Separa a hora atual em 6 dígitos decimais
2. **Conversão Binária**: Converte cada dígito para representação de 4 bits
3. **Atualização Visual**: Reflete o estado dos bits na grade de círculos
4. **Sincronização de Labels**: Atualiza os rótulos com os valores decimais atuais

**Exemplo**: Para 14:32:45
- Hora: 1, 4 → Minutos: 3, 2 → Segundos: 4, 5
- Cada dígito é convertido em 4 bits e representado visualmente

---

## 🚀 Como Usar

### Requisitos

- Android Studio (últimas versões recomendadas)
- SDK Android mínimo: API 26
- Gradle 7.0+

### Instalação

```bash
git clone https://github.com/seu-usuario/binary-clock-android.git
cd binary-clock-android
```

### Build e Execução

```bash
# Build do projeto
./gradlew build

# Instalar em emulador/dispositivo
./gradlew installDebug
```

---

## 📁 Estrutura do Projeto

```
binary-clock-android/
├── app/
│   ├── src/main/kotlin/
│   │   └── com/example/binaryclock/
│   │       ├── MainActivity.kt          # Activity principal com lógica
│   │       └── BinaryClockViewModel.kt  # ViewModel (se Compose)
│   ├── src/main/res/
│   │   ├── layout/
│   │   │   └── activity_main.xml        # Layout (se XML)
│   │   ├── values/
│   │   │   └── colors.xml
│   │   └── ...
│   └── AndroidManifest.xml
├── build.gradle
├── settings.gradle
└── README.md
```

---

## 💡 Conceitos Implementados

### Conversão Decimal para Binário (4 bits)

Para cada dígito decimal (0-9), a conversão segue:

```
Dígito 7 = 0111₂ → bits: [1, 1, 1, 0] (potências 1, 2, 4, 8)
```

### Atualização em Tempo Real

Utiliza `Handler.postDelayed()` ou `launchIn(Dispatchers.Main)` com Coroutines para atualizar a UI a cada segundo sem bloquear a thread principal.

---

## 🎨 Interface Visual

O layout segue o padrão de design proposto:

```
        ┌─────────────────────────────────┐
        │  ◉ ◉ ◉ ◉ ◉ ◉  8                │
        │  ◉ ◯ ◉ ◯ ◉ ◯  4                │
        │  ◉ ◉ ◉ ◯ ◉ ◉  2                │
        │  ◯ ◯ ◯ ◉ ◯ ◯  1                │
        │  1 4 3 2 4 5                    │
        └─────────────────────────────────┘
```

Legenda:
- `◉` = Círculo preenchido (bit 1)
- `◯` = Círculo vazio (bit 0)

---

## 📄 Licença

Este projeto foi desenvolvido como uma prova de conceito. Consulte o arquivo `LICENSE` para mais informações.

---

## 🤖 Geração com Antigravity

Este projeto foi **gerado automaticamente** utilizando o Antigravity, um gerador de código baseado em IA, a partir do prompt detalhado que descreve todos os requisitos de layout, lógica e funcionalidade do aplicativo.

> **Nota**: O código foi gerado como prototipagem rápida. Recomenda-se revisão e testes completos antes de uso em produção.

---

## 🔄 Melhorias Futuras

- [ ] Temas de cores customizáveis (claro/escuro)
- [ ] Seleção entre formatos 12h e 24h
- [ ] Animações de transição entre estados
- [ ] Testes unitários e de UI
- [ ] Publicação na Google Play Store

---

## 📞 Contato & Contribuições

Contribuições são bem-vindas! Abra uma issue ou pull request para sugestões e melhorias.

---

**Desenvolvido com Antigravity** 🚀
