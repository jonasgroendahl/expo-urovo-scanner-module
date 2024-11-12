import { StyleSheet, Text, View } from 'react-native';

import * as ExpoUrovoScannerModule from 'expo-urovo-scanner-module';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{ExpoUrovoScannerModule.hello()}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
