import SwiftUI

struct ContentView: View {
    @AppStorage("saved_host") private var savedHost: String = ""
    @State private var showPrompt = false
    @State private var currentURL: URL?

    var body: some View {
        NavigationView {
            Group {
                if let url = currentURL {
                    RecipeAppWebView(url: url)
                        .edgesIgnoringSafeArea(.all)
                } else {
                    VStack(spacing: 20) {
                        Text("WhiskFul")
                            .font(.largeTitle)
                            .fontWeight(.bold)
                            .foregroundColor(.white)

                        Button(action: { showPrompt = true }) {
                            Text(savedHost.isEmpty ? "Enter Server URL" : "Connect to \(savedHost)")
                                .foregroundColor(.white)
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color(red: 0x11/255, green: 0x11/255, blue: 0x11/255))
                                .cornerRadius(12)
                        }
                    }
                    .padding()
                    .navigationTitle("WhiskFul")
                }
            }
        }
        .onAppear {
            if savedHost.isNotEmpty {
                let urlString = savedHost.hasPrefix("http") ? savedHost : "https://\(savedHost)"
                currentURL = URL(string: urlString)
            } else {
                showPrompt = true
            }
        }
        .sheet(isPresented: $showPrompt) {
            HostPromptView { host in
                savedHost = host
                let urlString = host.hasPrefix("http") ? host : "https://\(host)"
                currentURL = URL(string: urlString)
            }
        }
    }
}

struct HostPromptView: View {
    @Environment(\.dismiss) var dismiss
    @State private var hostInput = ""

    var onSave: (String) -> Void

    var body: some View {
        NavigationView {
            VStack(spacing: 20) {
                Text("Enter Server Address")
                    .font(.headline)
                    .foregroundColor(.primary)

                TextField("https://192.168.1.100:3000", text: $hostInput)
                    .keyboardType(.URL)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .autocapitalization(.none)
                    .disableAutocorrection(true)

                Button("Connect") {
                    let trimmed = hostInput.trimmingCharacters(in: .whitespaces)
                    if trimmed.isEmpty { return }
                    onSave(trimmed)
                    dismiss()
                }
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color(red: 0x11/255, green: 0x11/255, blue: 0x11/255))
                .cornerRadius(12)
            }
            .padding()
            .navigationTitle("WhiskFul")
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}